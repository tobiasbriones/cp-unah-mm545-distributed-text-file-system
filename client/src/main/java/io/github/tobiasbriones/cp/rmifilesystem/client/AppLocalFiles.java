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

package io.github.tobiasbriones.cp.rmifilesystem.client;

import io.github.tobiasbriones.cp.rmifilesystem.impl.io.file.text.AppLocalTextFileRepository;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileRepository;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.DirectoryNode;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem.*;

/**
 * @author Tobias Briones
 */
public final class AppLocalFiles {
    private static final String FS_FILE_NAME = "fs.data";
    private static final String RELATIVE_ROOT = "fs";
    private static final String ROOT = System.getProperty("user.dir") + java.io.File.separator + RELATIVE_ROOT;

    public static FileSystem readFs() throws IOException {
        createRootIfNotExists();
        final var file = new java.io.File(ROOT, FS_FILE_NAME);

        try (ObjectInput input = new ObjectInputStream(new FileInputStream(file))) {
            return (FileSystem) input.readObject();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new FileSystem(DirectoryNode.of());
    }

    public static Map<File, LastUpdateStatus> readStatuses() throws IOException {
        createRootIfNotExists();
        final Path path = Path.of(ROOT, ".statuses.data");

        if (!Files.exists(path)) {
            return new HashMap<>(0);
        }

        try (ObjectInput input = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (Map<File, LastUpdateStatus>) input.readObject();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>(0);
        }
    }

    public static void saveFs(FileSystem system) throws IOException {
        createRootIfNotExists();
        final var file = new java.io.File(ROOT, FS_FILE_NAME);

        try (ObjectOutput output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(system);
        }
    }

    public static void saveStatuses(Map<File, LastUpdateStatus> statuses) throws IOException {
        createRootIfNotExists();
        final var file = new java.io.File(ROOT, ".statuses.data");

        try (ObjectOutput output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(statuses);
        }
    }

    public static TextFileRepository newTextFileRepository() {
        return new AppLocalTextFileRepository(Path.of(ROOT));
    }

    private static void createRootIfNotExists() throws IOException {
        createDirsIfNotExist(Path.of(ROOT));
    }

    private static void createDirsIfNotExist(Path path) throws IOException {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            Files.createDirectories(path);
        }
    }

    private AppLocalFiles() {}
}
