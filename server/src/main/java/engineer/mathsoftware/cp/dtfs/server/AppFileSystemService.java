// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.server;

import engineer.mathsoftware.cp.dtfs.FileSystemService;
import engineer.mathsoftware.cp.dtfs.OnFileUpdateListener;
import engineer.mathsoftware.cp.dtfs.RegistryService;
import engineer.mathsoftware.cp.dtfs.impl.io.file.text.AppLocalTextFileRepository;
import engineer.mathsoftware.cp.dtfs.io.File;
import engineer.mathsoftware.cp.dtfs.io.*;
import engineer.mathsoftware.cp.dtfs.io.file.Nothing;
import engineer.mathsoftware.cp.dtfs.io.file.Result;
import engineer.mathsoftware.cp.dtfs.io.file.Result.Failure;
import engineer.mathsoftware.cp.dtfs.io.file.Result.Success;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileContent;
import engineer.mathsoftware.cp.dtfs.io.node.DirectoryNode;
import engineer.mathsoftware.cp.dtfs.io.node.FileNode;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.ConnectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static engineer.mathsoftware.cp.dtfs.io.file.Nothing.Nothing;
import static engineer.mathsoftware.cp.dtfs.io.node.FileSystem.LastUpdateStatus;

/**
 * @author Tobias Briones
 */
public final class AppFileSystemService extends UnicastRemoteObject implements FileSystemService,
                                                                               RegistryService {
    @Serial
    private static final long serialVersionUID = 7826374551124313303L;
    private static final String RELATIVE_ROOT = "fs";
    static final String ROOT =
        System.getProperty("user.dir") + java.io.File.separator + RELATIVE_ROOT;
    private final Path rootPath;
    private final List<OnFileUpdateListener> clients;

    public AppFileSystemService() throws RemoteException {
        super();
        rootPath = Path.of(ROOT);
        clients = new ArrayList<>(10);

        System.out.println("Running on: " + ROOT);
    }

    private static void setChanged(File file) throws IOException {
        var statuses = loadStatuses();
        var status = LastUpdateStatus.of(file);

        // TODO files are coming file.txt not like /file.txt hence
        //  deleteFileStatus won't work!!!
        statuses.put(file, status);
        saveStatuses(statuses);
    }

    @Override
    public RealTimeFileSystem getRealTimeFileSystem() throws IOException {
        try {
            return loadRealTimeFileSystem();
        }
        catch (SecurityException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Result<TextFileContent> readTextFile(File.TextFile file) throws RemoteException {
        var repository = new AppLocalTextFileRepository(rootPath);
        var result = repository.get(file);
        return mapResult(result);
    }

    @Override
    public Result<Nothing> writeDirectory(Directory directory) throws RemoteException {
        var localFile = toLocalFile(directory.path());
        var path = localFile.toPath();

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                broadcastFSChange();
                return Success.of(Nothing);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Failure.of();
    }

    @Override
    public Result<Nothing> writeTextFile(TextFileContent content) throws RemoteException {
        var file = content.file();
        var path = toLocalFile(file.path()).toPath();
        var repository = new AppLocalTextFileRepository(rootPath);
        var result = Files.exists(path)
                     ? repository.set(content)
                     : repository.add(content);
        var clientResult = mapResult(result);

        if (clientResult instanceof Success) {
            onFileWrote(file);
        }
        return clientResult;
    }

    @Override
    public Result<Nothing> deleteFile(CommonFile file) throws RemoteException {
        var path = toLocalFile(file.path()).toPath();

        if (!Files.exists(path)) {
            return Failure.of(new IOException("File doesn't exist"));
        }

        try {
            Files.delete(path);
            onFileDeleted(file);
        }
        catch (DirectoryNotEmptyException e) {
            return Failure.of(new IOException("Directory not empty"));
        }
        catch (IOException e) {
            e.printStackTrace();
            return Failure.of();
        }
        return Success.of(Nothing);
    }

    @Override
    public boolean addOnFileUpdateListener(OnFileUpdateListener l) throws RemoteException {
        return clients.add(l);
    }

    @Override
    public boolean removeOnFileUpdateListener(OnFileUpdateListener l) throws RemoteException {
        return clients.remove(l);
    }

    @Override
    public boolean regObject(String name, Remote obj) {
        try {
            var registry = LocateRegistry.getRegistry();
            registry.bind(name, obj);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void onFileWrote(File file) {
        try {
            setChanged(file);
            broadcastFSChange();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onFileDeleted(CommonFile file) throws IOException {
        if (file instanceof File.TextFile f) {
            deleteFileStatus(f);
        }
        broadcastFSChange();
    }

    private void broadcastFSChange() throws IOException {
        var fs = loadRealTimeFileSystem();
        var remove = new ArrayList<OnFileUpdateListener>(0);

        for (var client : clients) {
            try {
                client.onFSChanged(fs);
            }
            catch (ConnectException e) {
                System.out.println(e.getMessage());
                remove.add(client);
            }
        }

        if (!remove.isEmpty()) {
            remove.forEach(clients::remove);

            System.out.println(remove.size() + " clients removed and " + clients.size() + " clients remaining");
        }
    }

    private static Path toPath() {
        return toPath(CommonPath.of());
    }

    private static Path toPath(CommonPath path) {
        return Path.of(toLocalFile(path).toURI());
    }

    private static JavaFile toLocalFile(CommonPath path) {
        return new JavaFile(ROOT, path.value());
    }

    private static <T extends Serializable> Result<T> mapResult(Result<T> result) {
        // Use proper logging
        Consumer<Throwable> logFailure = reason -> System.out.println(reason.getMessage());
        Function<Failure<T>, Failure<T>> fail = (f) -> {
            f.ifPresent(logFailure);
            return Failure.of();
        };
        return switch (result) {
            case Success<T> ignored -> result;
            case Failure<T> f -> fail.apply(f);
        };
    }

    private static RealTimeFileSystem loadRealTimeFileSystem() {
        final DirectoryNode root = loadRoot();
        final Map<File, LastUpdateStatus> statuses = loadStatuses();
        return new RealTimeFileSystem(root, statuses);
    }

    private static DirectoryNode loadRoot() {
        var rootPath = toPath();
        var node = DirectoryNode.of();
        loadNode(node, rootPath, rootPath);
        return node;
    }

    private static void loadNode(DirectoryNode node, Path path, Path rootPath) {
        FilenameFilter filter = (dir, name) -> !name.endsWith(".data");
        var directChildrenList = path.toFile().listFiles(filter);

        if (directChildrenList == null) {
            return;
        }

        var children = Arrays.stream(directChildrenList)
                             .map(java.io.File::toPath)
                             .map(PathType::of)
                             .map(pathType -> pathType.toRelativeCommonPathType(rootPath))
                             .filter(Optional::isPresent)
                             .map(Optional::get)
                             .toList();

        for (var child : children) {
            var commonFile = child.toCommonFile();
            var childPath = child.path();
            switch (commonFile) {
                case File f -> node.addChild(new FileNode(f));
                case Directory d -> {
                    var directoryChild = new DirectoryNode(d);
                    node.addChild(directoryChild);
                    loadNode(directoryChild, childPath, rootPath);
                }
            }
        }
    }

    private static void deleteFileStatus(File file) throws IOException {
        var statuses = loadStatuses();
        statuses.remove(file);
        saveStatuses(statuses);
    }

    private static Map<File, LastUpdateStatus> loadStatuses() {
        var path = Path.of(ROOT, ".statuses.data");

        if (!Files.exists(path)) {
            return new HashMap<>(0);
        }
        try (var input = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Map<File, LastUpdateStatus>) input.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>(0);
        }
    }

    private static void saveStatuses(Map<File, LastUpdateStatus> statuses) throws IOException {
        var path = Path.of(ROOT, ".statuses.data");
        try (var output = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            output.writeObject(statuses);
        }
    }

    private record PathType(Path path, boolean isDirectory) {
        Optional<CommonPathType> toRelativeCommonPathType(Path rootPath) {
            var relativePath = rootPath.relativize(path);
            return CommonPath.of(relativePath)
                             .map(commonPath -> new CommonPathType(
                                 path,
                                 commonPath,
                                 isDirectory
                             ));
        }

        static PathType of(Path path) {
            return new PathType(path, Files.isDirectory(path));
        }
    }

    private record CommonPathType(
        Path path,
        CommonPath commonPath,
        boolean isDirectory
    ) {
        CommonFile toCommonFile() {
            return isDirectory ? Directory.of(commonPath) : File.of(commonPath);
        }
    }
}
