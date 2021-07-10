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

package io.github.tobiasbriones.cp.rmifilesystem.model.io.node;

import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectoryNodeTest {
    private DirectoryNode node;

    DirectoryNodeTest() {
        node = null;
    }

    @BeforeEach
    void setUp() {
        node = new DirectoryNode();
    }

    @Test
    void isRoot() {
        assertThat(node.isRoot(), is(true));
    }

    @Test
    void getParent() {
        final var dirNode = new DirectoryNode(new Directory("fs"));

        assertThat(
            "The default node's parent is empty",
            node.getParent(),
            is(Optional.empty())
        );

        assertThat(
            "A single node's parent is empty",
            dirNode.getParent(),
            is(Optional.empty())
        );
    }

    @Test
    void setParent() {
        final var dirNode = new DirectoryNode(new Directory("fs"));

        dirNode.setParent(node);

        assertThat(
            "A single node's parent is root when setting its parent to root",
            dirNode.getParent(),
            is(Optional.of(node))
        );

        assertThat(
            "The parent node child is added",
            node.hasChild(dirNode.commonFile()),
            is(true)
        );
    }

    @Test
    void getChildren() {
        assertThat(
            "The default node has no children",
            node.getChildren().size(),
            is(0)
        );
    }

    @Test
    void addChild() {
        final var c1 = new DirectoryNode(new Directory("/fs"));
        final var c2 = new FileNode(new File.TextFile("/file1.txt"));

        node.addChild(c1);
        assertTrue(node.hasChild(c1.commonFile()), "A directory node is added as child");

        node.addChild(c2);
        assertTrue(node.hasChild(c2.commonFile()), "A file node is added as child");
    }

    @Test
    void addChildren() {
        final var c1 = new DirectoryNode(new Directory("/fs"));
        final var c2 = new FileNode(new File.TextFile("/file1.txt"));
        final var c3 = new FileNode(new File.TextFile("/file2.txt"));

        node.addChildren(c1, c2, c3);

        assertThat(
            "The default node has children when adding them as children",
            node.getChildren(),
            containsInAnyOrder(c1, c2, c3)
        );

        assertThat(
            "Directory's parent is the default node",
            c1.getParent(),
            is(Optional.of(node))
        );
    }

    @Test
    void hasChild() {
        final var c1 = new DirectoryNode(new Directory("/fs"));
        final var c2 = new FileNode(new File.TextFile("/file1.txt"));

        node.addChildren(c1, c2);
        assertTrue(node.hasChild(c1.commonFile()), "Node has directory child after adding it");
        assertTrue(node.hasChild(c2.commonFile()), "Node has file child after adding it");
    }

    @Test
    void testCircularParent() {
        final var c1 = new DirectoryNode(new Directory("/fs"));
        final var c2 = new DirectoryNode(new Directory("/fs/dir1"));

        // Build /fs/dir1
        c1.setParent(node);
        c2.setParent(c1);

        assertThrows(CircularParentException.class, () -> node.setParent(c2));
        assertThrows(CircularParentException.class, () -> node.setParent(c1));
        assertThrows(CircularParentException.class, () -> c1.setParent(c2));
    }

    @Test
    void testInvalidChild() {
        final var c1 = new DirectoryNode(new Directory("/fs"));
        final var c2 = new DirectoryNode(new Directory("/fs/dir1"));

        // Build /fs/dir1
        node.addChild(c1);
        c1.addChild(c2);

        assertThrows(InvalidChildException.class, () -> c2.addChild(node));
        assertThrows(InvalidChildException.class, () -> c1.addChild(node));
        assertThrows(InvalidChildException.class, () -> c2.addChild(c1));
    }

    @Test
    void testSampleFs() {
        final DirectoryNode root = new DirectoryNode(new Directory("root"));
        final String expectedString = "root";
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
        final var dir1 = new DirectoryNode(new Directory("root/dir1"));
        final var dir2 = new DirectoryNode(new Directory("root/dir2"));

        dir1.addChild(new FileNode(new File.TextFile("root/dir1/file-x.txt")));
        dir2.addChild(new FileNode(new File.TextFile("root/dir2/file-y.txt")));

        // Nested folders
        final var dir11 = new DirectoryNode(new Directory("root/dir1/dir11"));
        final var nestedFile = new FileNode(new File.TextFile("root/dir1/dir11/nested.txt"));

        dir11.addChild(nestedFile);
        dir1.addChild(dir11);

        // Root files
        final var file1 = new FileNode(new File.TextFile("root/file1.txt"));
        final var file2 = new FileNode(new File.TextFile("root/file2.txt"));

        root.addChildren(dir1, dir2, file1, file2);

        assertThat(root.toString(), is(expectedString));
        assertThat(root.toRecursiveString(), is(recursiveExpected));
    }
}
