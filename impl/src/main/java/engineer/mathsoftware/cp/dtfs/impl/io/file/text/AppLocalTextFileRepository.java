// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.impl.io.file.text;

import engineer.mathsoftware.cp.dtfs.io.file.Nothing;
import engineer.mathsoftware.cp.dtfs.io.file.Result;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileContent;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static engineer.mathsoftware.cp.dtfs.io.File.TextFile;
import static engineer.mathsoftware.cp.dtfs.io.file.Nothing.Nothing;

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
        var path = CommonPaths.toPath(root, file.path());

        try {
            createRootIfNotExists();
            requireFileExists(path);
            var value = Files.readString(path);
            return Result.Success.of(new TextFileContent(file, value));
        }
        catch (IOException e) {
            return Result.Failure.of(e);
        }
    }

    @Override
    public Result<Nothing> set(TextFileContent content) {
        var file = content.file();
        var path = CommonPaths.toPath(root, file.path());

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
        var file = content.file();
        var path = CommonPaths.toPath(root, file.path());

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
        var path = CommonPaths.toPath(root, file.path());

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
        var path = CommonPaths.toPath(root, file.path());
        return Files.exists(path);
    }

    private void createRootIfNotExists() throws IOException {
        createDirsIfNotExists(root);
    }

    private static void createFileIfNotExists(Path path) throws IOException {
        if (!Files.exists(path) || Files.isDirectory(path)) {
            var parent = path.getParent();
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
            var msg = "File %s does not exist".formatted(path);
            throw new FileNotFoundException(msg);
        }
    }

    private static void requireFileNotExists(Path path) throws IOException {
        if (Files.exists(path)) {
            var msg = "File %s already exist".formatted(path);
            throw new IOException(msg);
        }
    }
}
