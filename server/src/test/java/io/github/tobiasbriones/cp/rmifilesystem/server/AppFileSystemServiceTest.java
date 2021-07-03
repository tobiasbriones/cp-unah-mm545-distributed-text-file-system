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

import io.github.tobiasbriones.cp.rmifilesystem.model.ClientFile;
import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import io.github.tobiasbriones.cp.rmifilesystem.model.LocalClientFile;
import io.github.tobiasbriones.cp.rmifilesystem.model.RemoteClientFile;
import io.github.tobiasbriones.cp.rmifilesystem.server.AppFileSystemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;

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
        final var root = new File(AppFileSystemService.ROOT);

        prepareFsRoot(root);
        setUpDefaultFs(root);
    }

    @Test
    @DisplayName("Read the default file structure")
    void readFs() throws RemoteException {
        final List<RemoteClientFile> fs = service.getFileSystem();

        assertNotNull(fs, "Expect the file system is not null");

        final List<String> fsPaths = fs.stream().map(ClientFile::getRelativePath).toList();
        final var dir1 = new File(DIR_1_NAME);
        final var expectedFiles = List.of(
            new File(DIR_1_NAME),
            new File(TEXT_FILE_1_NAME),
            new File(dir1, TEXT_FILE_2_NAME)
        );
        final var expected = expectedFiles.stream().map(File::toString).toList();

        assertThat(fsPaths, containsInAnyOrder(expected.toArray()));
    }

    @Test
    @DisplayName("Read a specific text file")
    void readFile() throws IOException {
        final var file = new File(TEXT_FILE_1_NAME);
        final var clientFile = new LocalClientFile(file);
        final var content = service.readTextFile(clientFile);

        assertNotNull(content, "Expect the file content is not null");
        assertThat(content, is(TEXT_FILE_1_CONTENT));
    }

    @Test
    @DisplayName("Create a new directory")
    void writeDir() throws IOException {
        final var dir = new File("new-dir");
        final var clientDir = new LocalClientFile(dir);

        service.writeDir(clientDir);
        final var root = new File(AppFileSystemService.ROOT);
        final var newDir = new File(root, "new-dir");

        assertTrue(
            newDir.exists(),
            "Expect the service created the new directory"
        );
    }

    @Test
    @DisplayName("Write to a specific text file")
    void writeFile() throws IOException {
        final var file = new File(TEXT_FILE_1_NAME);
        final var clientFile = new LocalClientFile(file);
        final var content = "New file content";

        service.writeTextFile(clientFile, content);
        final var newContent = service.readTextFile(clientFile);

        assertNotNull(
            newContent,
            "Expect the new file content is null"
        );
        assertThat(newContent, is(content));
    }

    private static void prepareFsRoot(File root) {
        if (root.exists()) {
            try (var stream = Files.walk(root.toPath())) {
                stream.sorted(Comparator.reverseOrder())
                      .map(Path::toFile)
                      .forEach(File::delete);
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
    private static void setUpDefaultFs(File root) {
        final var dir1 = new File(root, DIR_1_NAME);
        final var file1 = new File(root, TEXT_FILE_1_NAME);
        final var file2 = new File(dir1, TEXT_FILE_2_NAME);

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
