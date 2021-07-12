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

package io.github.tobiasbriones.cp.rmifilesystem.server;

import io.github.tobiasbriones.cp.rmifilesystem.model.*;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.*;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.DirectoryNode;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileNode;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem;

import java.io.IOException;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * @author Tobias Briones
 */
public final class AppFileSystemService extends UnicastRemoteObject implements FileSystemService {
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

    private final List<OnFileUpdateListener> clients;

    public AppFileSystemService() throws RemoteException {
        super();
        clients = new ArrayList<>(10);

        System.out.println("Running on: " + ROOT);
    }

    @Override
    public FileSystem getFileSystem() throws IOException {
        try {
            return new FileSystem(loadRoot());
        }
        catch (SecurityException e) {
            throw new IOException(e);
        }
    }

    @Override
    public String readTextFile(File.TextFile file) throws IOException {
        final JavaFile localFile = toLocalFile(file.path());
        return Files.readString(localFile.toPath());
    }

    @Override
    public void writeDir(Directory directory) throws IOException {
        final JavaFile localFile = toLocalFile(directory.path());

        if (!localFile.mkdirs()) {
            final var msg = "Fail to make dirs";
            throw new IOException(msg);
        }
    }

    @Override
    public void writeTextFile(File.TextFile file, String content) throws IOException {
        final JavaFile localFile = toLocalFile(file.path());

        Files.writeString(localFile.toPath(), content);
        broadcastUpdate(file);
    }

    @Override
    public boolean addOnFileUpdateListener(OnFileUpdateListener l) throws RemoteException {
        return clients.add(l);
    }

    private void broadcastUpdate(File file) {
        for (final var client : clients) {
            try {
                client.onFileChanged(new FileSystem.Status(file, true));
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private static DirectoryNode loadRoot() {
        final Path rootPath = toPath();
        final DirectoryNode node = DirectoryNode.of();

        loadNode(node, rootPath, rootPath);
        return node;
    }

    private static void loadNode(DirectoryNode node, Path path, Path rootPath) {
        final var directChildrenList = path.toFile().listFiles();

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
}
