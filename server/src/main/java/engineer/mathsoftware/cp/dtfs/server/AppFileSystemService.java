/*
 * Copyright (c) 2021 Tobias Briones. All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * This file is part of Course Project at UNAH-MM545: Distributed Text File
 * System.
 *
 * This source code is licensed under the BSD-3-Clause License found in the
 * LICENSE file in the root directory of this source tree or at
 * https://opensource.org/licenses/BSD-3-Clause.
 */

package engineer.mathsoftware.cp.dtfs.server;

import engineer.mathsoftware.cp.dtfs.impl.io.file.text.AppLocalTextFileRepository;
import engineer.mathsoftware.cp.dtfs.FileSystemService;
import engineer.mathsoftware.cp.dtfs.OnFileUpdateListener;
import engineer.mathsoftware.cp.dtfs.RegistryService;
import engineer.mathsoftware.cp.dtfs.io.*;
import engineer.mathsoftware.cp.dtfs.io.File;
import engineer.mathsoftware.cp.dtfs.io.file.Nothing;
import engineer.mathsoftware.cp.dtfs.io.file.Result;
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
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.function.Consumer;

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
    static final String ROOT = System.getProperty("user.dir") + java.io.File.separator + RELATIVE_ROOT;

    private record PathType(Path path, boolean isDirectory) {
        static PathType of(Path path) {
            return new PathType(path, Files.isDirectory(path));
        }

        Optional<CommonPathType> toRelativeCommonPathType(Path rootPath) {
            final Path relativePath = rootPath.relativize(path);
            return CommonPath.of(relativePath)
                             .map(commonPath -> new CommonPathType(path, commonPath, isDirectory));
        }
    }

    private record CommonPathType(Path path, CommonPath commonPath, boolean isDirectory) {
        CommonFile toCommonFile() {
            return isDirectory ? Directory.of(commonPath) : File.of(commonPath);
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

    private final Path rootPath;
    private final List<OnFileUpdateListener> clients;

    public AppFileSystemService() throws RemoteException {
        super();
        rootPath = Path.of(ROOT);
        clients = new ArrayList<>(10);

        System.out.println("Running on: " + ROOT);
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
        final var repository = new AppLocalTextFileRepository(rootPath);
        final var result = repository.get(file);
        return mapResult(result);
    }

    @Override
    public Result<Nothing> writeDirectory(Directory directory) throws RemoteException {
        final JavaFile localFile = toLocalFile(directory.path());
        final Path path = localFile.toPath();

        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                broadcastFSChange();
                return Result.Success.of(Nothing);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Result.Failure.of();
    }

    @Override
    public Result<Nothing> writeTextFile(TextFileContent content) throws RemoteException {
        final File file = content.file();
        final Path path = toLocalFile(file.path()).toPath();
        final var repository = new AppLocalTextFileRepository(rootPath);
        final var result = Files.exists(path) ? repository.set(content) : repository.add(content);
        final var clientResult = mapResult(result);

        if (clientResult instanceof Result.Success) {
            onFileWrote(file);
        }
        return clientResult;
    }

    @Override
    public Result<Nothing> deleteFile(CommonFile file) throws RemoteException {
        final Path path = toLocalFile(file.path()).toPath();

        if (!Files.exists(path)) {
            return Result.Failure.of(new IOException("File doesn't exist"));
        }

        try {
            Files.delete(path);
            onFileDeleted(file);
        }
        catch (DirectoryNotEmptyException e) {
            return Result.Failure.of(new IOException("Directory not empty"));
        }
        catch (IOException e) {
            e.printStackTrace();
            return Result.Failure.of();
        }
        return Result.Success.of(Nothing);
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
            final Registry registry = LocateRegistry.getRegistry();

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
        final RealTimeFileSystem fs = loadRealTimeFileSystem();
        final Collection<OnFileUpdateListener> remove = new ArrayList<>(0);

        for (final var client : clients) {
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

    private static <T extends Serializable> Result<T> mapResult(Result<T> result) {
        final Consumer<Throwable> logFailure = reason -> System.out.println(reason.getMessage()); // Use proper logging

        // Switch pattern matching for Java17 ep+
        if (result instanceof Result.Failure<T> f) {
            f.ifPresent(logFailure);
            return Result.Failure.of();
        }
        return result;
    }

    private static RealTimeFileSystem loadRealTimeFileSystem() {
        final DirectoryNode root = loadRoot();
        final Map<File, LastUpdateStatus> statuses = loadStatuses();
        return new RealTimeFileSystem(root, statuses);
    }

    private static DirectoryNode loadRoot() {
        final Path rootPath = toPath();
        final DirectoryNode node = DirectoryNode.of();

        loadNode(node, rootPath, rootPath);
        return node;
    }

    private static void loadNode(DirectoryNode node, Path path, Path rootPath) {
        final FilenameFilter filter = (dir, name) -> !name.endsWith(".data");
        final var directChildrenList = path.toFile().listFiles(filter);

        if (directChildrenList == null) {
            return;
        }

        final List<CommonPathType> children = Arrays.stream(directChildrenList)
                                                    .map(java.io.File::toPath)
                                                    .map(PathType::of)
                                                    .map(pathType -> pathType.toRelativeCommonPathType(rootPath))
                                                    .filter(Optional::isPresent)
                                                    .map(Optional::get)
                                                    .toList();

        for (final CommonPathType child : children) {
            final CommonFile commonFile = child.toCommonFile();
            final Path childPath = child.path();

            // Can use switch pattern matching for Java17 ep +
            if (commonFile instanceof File f) {
                node.addChild(new FileNode(f));
            }
            else if (commonFile instanceof Directory d) {
                final var directoryChild = new DirectoryNode(d);

                node.addChild(directoryChild);
                loadNode(directoryChild, childPath, rootPath);
            }
        }
    }

    private static void setChanged(File file) throws IOException {
        final Map<File, LastUpdateStatus> statuses = loadStatuses();
        final LastUpdateStatus status = LastUpdateStatus.of(file);

        // TODO files are coming file.txt not like /file.txt hence deleteFileStatus won't work!!!
        statuses.put(file, status);
        saveStatuses(statuses);
    }

    private static void deleteFileStatus(File file) throws IOException {
        final Map<File, LastUpdateStatus> statuses = loadStatuses();

        statuses.remove(file);
        saveStatuses(statuses);
    }

    private static Map<File, LastUpdateStatus> loadStatuses() {
        final Path path = Path.of(ROOT, ".statuses.data");

        if (!Files.exists(path)) {
            return new HashMap<>(0);
        }
        try (ObjectInput input = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Map<File, LastUpdateStatus>) input.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>(0);
        }
    }

    private static void saveStatuses(Map<File, LastUpdateStatus> statuses) throws IOException {
        final Path path = Path.of(ROOT, ".statuses.data");

        try (ObjectOutput output = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            output.writeObject(statuses);
        }
    }
}
