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

import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.Result;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileContent;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.DirectoryNode;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileNode;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Tobias Briones
 */
@DisplayName("Test basic AppFileSystemService functionality")
class AppFileSystemServiceTest {
    private static final String DIR_1_NAME = "dir1";
    private static final String TEXT_FILE_1_NAME = "file1.txt";
    private static final String TEXT_FILE_1_CONTENT = "This is file 1";
    private static final String TEXT_FILE_2_NAME = "file2.txt";
    private static final String TEXT_FILE_2_CONTENT = "And this is file 2";
    private FileSystemService service;

    AppFileSystemServiceTest() {
        service = null;
    }

    @BeforeEach
    void setUp() throws RemoteException {
        service = new AppFileSystemService();
        final var root = new java.io.File(AppFileSystemService.ROOT);

        prepareFsRoot(root);
        setUpDefaultFs(root);
    }

    @Test
    @DisplayName("Read the default file system")
    void readFs() throws IOException {
        final FileSystemService.RealTimeFileSystem rtfs = service.getRealTimeFileSystem();
        final FileSystem fs = FileSystems.buildFileSystem(rtfs, new HashMap<>(0));

        assertNotNull(fs, "Expects the file system to not be null");

        final DirectoryNode actual = fs.getRoot();
        final var root = new DirectoryNode(Directory.of());
        final var dir1 = new DirectoryNode(new Directory("/" + DIR_1_NAME));
        final var file11 = new FileNode(new File.TextFile("/" + DIR_1_NAME + "/" + TEXT_FILE_1_NAME));
        final var file1 = new FileNode(new File.TextFile("/" + TEXT_FILE_1_NAME));

        root.addChildren(dir1, file1);
        dir1.addChild(file11);

        assertTrue(actual.hasChild(dir1.commonFile()));
        assertTrue(actual.hasChild(file1.commonFile()));
        assertTrue(dir1.hasChild(file11.commonFile()));
    }

    @Test
    @DisplayName("Read a specific text file")
    void readFile() throws IOException {
        final var file = new File.TextFile("/" + TEXT_FILE_1_NAME);
        final Result<TextFileContent> contentResult = service.readTextFile(file);

        assertThat(contentResult instanceof Result.Success, is(true));

        final TextFileContent actual = contentResult.value();

        assertThat(actual.value(), is(TEXT_FILE_1_CONTENT));
    }

    @Test
    @DisplayName("Create a new directory")
    void writeDir() throws IOException {
        final var dir = new Directory("/new-dir");

        service.writeDirectory(dir);

        assertTrue(
            Files.exists(Path.of(AppFileSystemService.ROOT, "new-dir")),
            "Expects the service created the new directory"
        );
    }

    @Test
    @DisplayName("Write to a specific text file")
    void writeFile() throws IOException {
        final var file = new File.TextFile("/" + TEXT_FILE_1_NAME);
        final var contentValue = "New file content";
        final var content = new TextFileContent(file, contentValue);

        service.writeTextFile(content);
        final Result<TextFileContent> newContentResult = service.readTextFile(file);

        assertThat(newContentResult instanceof Result.Success, is(true));

        final TextFileContent actual = newContentResult.value();

        assertThat(actual, is(content));
    }

    private static void prepareFsRoot(java.io.File root) {
        if (root.exists()) {
            try (var stream = Files.walk(root.toPath())) {
                stream.sorted(Comparator.reverseOrder())
                      .map(Path::toFile)
                      .forEach(java.io.File::delete);
            }
            catch (IOException e) {
                final var msg = "Fail to delete root directory before running tests: " + e;
                throw new RuntimeException(msg);
            }
        }
        if (!root.mkdir()) {
            final var msg = "Fail to setup tests: Couldn't make the root directory";
            throw new RuntimeException(msg);
        }
    }

    /**
     * Setup this file structure:
     *
     * - /
     * - /dir1
     * -   /file2.txt
     * - /file1.txt
     *
     * @param root root file for the FS
     */
    private static void setUpDefaultFs(java.io.File root) {
        final var dir1 = new java.io.File(root, DIR_1_NAME);
        final var file1 = new java.io.File(root, TEXT_FILE_1_NAME);
        final var file2 = new java.io.File(dir1, TEXT_FILE_2_NAME);

        if (!dir1.mkdir()) {
            final var msg = "Fail to create test directory: " + dir1;
            throw new RuntimeException(msg);
        }

        try {
            Files.writeString(file1.toPath(), TEXT_FILE_1_CONTENT);
            Files.writeString(file2.toPath(), TEXT_FILE_2_CONTENT);
        }
        catch (IOException e) {
            final var msg = "Fail to setup default FS: " + e;
            throw new RuntimeException(msg);
        }
    }
}
