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

package io.github.tobiasbriones.cp.rmifilesystem.client.io;

import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.JavaFile;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.DirectoryNode;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Tobias Briones
 */
public final class AppLocalFiles {
    private static final String FS_FILE_NAME = "fs.data";
    private static final String RELATIVE_ROOT = "fs";
    private static final String ROOT = System.getProperty("user.dir") + java.io.File.separator + RELATIVE_ROOT;

    public static FileSystem readFs() throws IOException {
        check();
        final var file = new java.io.File(ROOT, FS_FILE_NAME);

        try (ObjectInput input = new ObjectInputStream(new FileInputStream(file))) {
            return (FileSystem) input.readObject();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new FileSystem(DirectoryNode.of());
    }

    public static void saveFs(FileSystem system) throws IOException {
        check();
        final var file = new java.io.File(ROOT, FS_FILE_NAME);

        try (ObjectOutput output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(system);
        }
    }

    public static Optional<String> readFile(File.TextFile file) throws IOException {
        check();
        final Path path = CommonPaths.toPath(ROOT, file.path());

        if (Files.exists(path)) {
            return Optional.ofNullable(Files.readString(path));
        }
        return Optional.empty();
    }

    public static void writeFile(File.TextFile file, CharSequence content) throws IOException {
        check();
        final Path path = CommonPaths.toPath(ROOT, file.path());

        checkFile(path);
        Files.writeString(path, content);
    }

    public static void writeDirectory(Directory directory) throws IOException {
        check();
        final Path path = CommonPaths.toPath(ROOT, directory.path());

        if (Files.isDirectory(path)) {
            return;
        }
        Files.createDirectories(path);
    }

    private static void check() throws IOException {
        checkDirs(Path.of(ROOT));
    }

    private static void checkFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            final Path parent = path.getParent();

            checkDirs(parent);
            Files.createFile(path);
        }
    }

    private static void checkDirs(Path path) throws IOException {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            Files.createDirectories(path);
        }
    }

    private AppLocalFiles() {}
}
