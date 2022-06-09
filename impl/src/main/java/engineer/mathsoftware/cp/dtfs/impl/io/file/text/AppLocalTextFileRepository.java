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

package engineer.mathsoftware.cp.dtfs.impl.io.file.text;

import com.github.tobiasbriones.cp.rmifilesystem.model.io.file.Nothing;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.file.Result;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileContent;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.tobiasbriones.cp.rmifilesystem.model.io.file.Nothing.Nothing;
import static com.github.tobiasbriones.cp.rmifilesystem.model.io.File.TextFile;

/**
 * @author Tobias Briones
 */
public class AppLocalTextFileRepository implements TextFileRepository {
    private final Path root;

    public AppLocalTextFileRepository(Path fsRootPath) {
        root = fsRootPath;
    }

    @Override
    public Result<TextFileContent> get(TextFile file) {
        final Path path = CommonPaths.toPath(root, file.path());

        try {
            createRootIfNotExists();
            requireFileExists(path);
            final String value = Files.readString(path);
            return Result.Success.of(new TextFileContent(file, value));
        }
        catch (IOException e) {
            return Result.Failure.of(e);
        }
    }

    @Override
    public Result<Nothing> set(TextFileContent content) {
        final TextFile file = content.file();
        final Path path = CommonPaths.toPath(root, file.path());

        try {
            createRootIfNotExists();
            requireFileExists(path);
            Files.writeString(path, content.value());
            return Result.Success.of(Nothing);
        }
        catch (IOException e) {
            return Result.Failure.of(e);
        }
    }

    @Override
    public Result<Nothing> add(TextFileContent content) {
        final TextFile file = content.file();
        final Path path = CommonPaths.toPath(root, file.path());

        try {
            createRootIfNotExists();
            requireFileNotExists(path);
            createFileIfNotExists(path);
            Files.writeString(path, content.value());
            return Result.Success.of(Nothing);
        }
        catch (IOException e) {
            return Result.Failure.of(e);
        }
    }

    @Override
    public Result<Nothing> remove(TextFile file) {
        final Path path = CommonPaths.toPath(root, file.path());

        try {
            createRootIfNotExists();
            requireFileExists(path);
            Files.delete(path);
            return Result.Success.of(Nothing);
        }
        catch (IOException e) {
            return Result.Failure.of(e);
        }
    }

    @Override
    public boolean exists(TextFile file) {
        final Path path = CommonPaths.toPath(root, file.path());
        return Files.exists(path);
    }

    private void createRootIfNotExists() throws IOException {
        createDirsIfNotExists(root);
    }

    private static void createFileIfNotExists(Path path) throws IOException {
        if (!Files.exists(path) || Files.isDirectory(path)) {
            final Path parent = path.getParent();

            createDirsIfNotExists(parent);
            Files.createFile(path);
        }
    }

    private static void createDirsIfNotExists(Path path) throws IOException {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            Files.createDirectories(path);
        }
    }

    private static void requireFileExists(Path path) throws FileNotFoundException {
        if (!Files.exists(path)) {
            final var msg = "File %s does not exist".formatted(path);
            throw new FileNotFoundException(msg);
        }
    }

    private static void requireFileNotExists(Path path) throws IOException {
        if (Files.exists(path)) {
            final var msg = "File %s already exist".formatted(path);
            throw new IOException(msg);
        }
    }
}
