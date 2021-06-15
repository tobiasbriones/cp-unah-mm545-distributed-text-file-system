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

package io.github.tobiasbriones.cp.rmifilesystem;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tobias Briones
 */
public final class AppFileSystemService extends UnicastRemoteObject implements FileSystemService {
    @Serial
    private static final long serialVersionUID = 7826374551124313303L;
    private static final String RELATIVE_ROOT = "fs";
    static final String ROOT = System.getProperty("user.dir") + File.separator + RELATIVE_ROOT;
    private final List<OnFileUpdateListener> clients;

    public AppFileSystemService() throws RemoteException {
        super();
        clients = new ArrayList<>(10);

        System.out.println("Running on: " + ROOT);
    }

    @Override
    public List<File> getFileSystem() {
        final var root = new File(ROOT);
        return recursiveListFilesOf(root).stream()
                                         .map(AppFileSystemService::toRelativeFile)
                                         .toList();
    }

    @Override
    public List<File> getInvalidFiles(String clientName) throws IOException {
        return Clients.loadInvalidFiles(clientName);
    }

    @Override
    public String readTextFile(File file) throws IOException {
        return Files.readString(toAbsoluteFile(file).toPath());
    }

    @Override
    public String readTextFile(File file, String clientName) throws IOException {
        Clients.setValid(file, clientName);
        return Files.readString(toAbsoluteFile(file).toPath());
    }

    @Override
    public void writeDir(File file) throws IOException {
        final var absFile = toAbsoluteFile(file);

        if (!absFile.mkdirs()) {
            final var msg = "Fail to create directory";
            throw new IOException(msg);
        }
        broadcastUpdate(file);
    }

    @Override
    public void writeTextFile(File file, String content) throws IOException {
        Files.writeString(toAbsoluteFile(file).toPath(), content);
        broadcastUpdate(file);
    }

    @Override
    public void addOnFileUpdateListener(OnFileUpdateListener l) throws RemoteException {
        clients.add(l);
    }

    private void broadcastUpdate(File file) {
        Clients.setInvalid(file);

        for (var client : clients) {
            try {
                client.onFileChanged(file);
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private static File toAbsoluteFile(File file) {
        return new File(ROOT, String.valueOf(file.toPath()));
    }

    private static List<File> recursiveListFilesOf(File root) {
        final var list = root.listFiles();

        if (list == null) {
            return List.of();
        }
        final var files = new ArrayList<File>(5);

        for (var file : list) {
            files.add(file);

            if (file.isDirectory()) {
                files.addAll(recursiveListFilesOf(file));
            }
        }
        return files;
    }

    private static File toRelativeFile(File file) {
        final var path = file.getAbsolutePath();

        if (!path.startsWith(ROOT)) {
            return new File("");
        }
        final var relPath = path.substring(ROOT.length() + 1);
        return new File(relPath);
    }
}
