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

package io.github.tobiasbriones.cp.rmifilesystem.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Tobias Briones
 */
class FileTreeTest {
    private FileTree<LocalClientFile> tree;

    FileTreeTest() {
        tree = null;
    }

    @BeforeEach
    void setUp() {
        tree = new FileTree<>(new LocalClientFile(new File("root")));
    }

    @Test
    @DisplayName("Build a simple file tree")
    void buildSimpleTree() {
        final FileTree.Node<LocalClientFile> root = tree.getRoot();
        final String expectedString = """
                                      dir1
                                      dir2
                                      file1.txt
                                      file2.txt
                                      """.trim();
        final String recursiveExpected = """
                                         root
                                           dir1
                                             file-x.txt
                                             dir11
                                               nested.txt
                                           dir2
                                             file-y.txt
                                           file1.txt
                                           file2.txt
                                         """;

        // Two folders
        final var dir1 = new FileTree.Node<>(new LocalClientFile("dir1"));
        final var dir2 = new FileTree.Node<>(new LocalClientFile("dir2"));

        dir1.addChild(new FileTree.Node<>(new LocalClientFile("file-x.txt")));
        dir2.addChild(new FileTree.Node<>(new LocalClientFile("file-y.txt")));

        // Nested folders
        final var dir11 = new FileTree.Node<>(new LocalClientFile("dir11"));
        final var nestedFile = new FileTree.Node<>(new LocalClientFile("nested.txt"));

        dir11.addChild(nestedFile);
        dir1.addChild(dir11);

        // Root files
        final var file1 = new FileTree.Node<>(new LocalClientFile("file1.txt"));
        final var file2 = new FileTree.Node<>(new LocalClientFile("file2.txt"));

        root.addChildren(dir1, dir2, file1, file2);

        assertThat(root.toString(), is(expectedString));
        assertThat(root.toRecursiveString(), is(recursiveExpected));
    }
}
