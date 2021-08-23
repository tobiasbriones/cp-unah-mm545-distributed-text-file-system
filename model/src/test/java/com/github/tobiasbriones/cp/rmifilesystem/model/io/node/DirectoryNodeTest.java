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

package com.github.tobiasbriones.cp.rmifilesystem.model.io.node;

import com.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link DirectoryNode} class.
 *
 * @author Tobias Briones
 */
@DisplayName("Test the DirectoryNode class")
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
    @DisplayName("Test accessor method: isRoot")
    void isRoot() {
        assertThat(node.isRoot(), is(true));
    }

    @Test
    @DisplayName("Test accessor method: getParent")
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
    @DisplayName("Test mutator method: setParent")
    void setParent() {
        final var dirNode = new DirectoryNode(new Directory("/fs"));

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
    @DisplayName("Test accessor method: getChildren")
    void getChildren() {
        assertThat(
            "The default node has no children",
            node.getChildren().size(),
            is(0)
        );
    }

    @Test
    @DisplayName("Test method: addChild")
    void addChild() {
        final var c1 = new DirectoryNode(new Directory("/fs"));
        final var c2 = new FileNode(new File.TextFile("/file1.txt"));

        node.addChild(c1);
        assertTrue(node.hasChild(c1.commonFile()), "A directory node is added as child");

        node.addChild(c2);
        assertTrue(node.hasChild(c2.commonFile()), "A file node is added as child");
    }

    @Test
    @DisplayName("Test method: addChildren")
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
    @DisplayName("Test accessor method: hasChild")
    void hasChild() {
        final var c1 = new DirectoryNode(new Directory("/fs"));
        final var c2 = new FileNode(new File.TextFile("/file1.txt"));

        node.addChildren(c1, c2);
        assertTrue(node.hasChild(c1.commonFile()), "Node has directory child after adding it");
        assertTrue(node.hasChild(c2.commonFile()), "Node has file child after adding it");
    }

    @Test
    @DisplayName("Test method: removeChild")
    void removeChild() {
        final BiFunction<DirectoryNode, DirectoryNode, Boolean> isChildRemoved = (dir, child)
            -> !dir.hasChild(child.commonFile()) && child.getParent().equals(Optional.empty());

        final var d1 = new DirectoryNode(new Directory("/fs"));
        final var f1 = new FileNode(new File.TextFile("/file1.txt"));
        final var d2 = new DirectoryNode(new Directory("/fs/dir1"));
        final var f2 = new FileNode(new File.TextFile("/fs/dir1/file2.txt"));

        node.addChildren(d1, f1);
        d1.addChild(d2);
        d2.addChild(f2);

        assertTrue(node.removeChild(f1), "Claims to had removed file1.txt");
        assertFalse(node.hasChild(f1.commonFile()), "file1.txt was actually removed");

        assertTrue(d1.removeChild(d2), "Claims to had removed dir1");
        assertTrue(isChildRemoved.apply(d1, d2), "dir1 was actually removed from /fs");

        assertTrue(node.hasChild(d1.commonFile()), "Node has its other untouched children");
    }

    @Test
    @DisplayName("Setup circular parents and expect them to be rejected")
    void testCircularParent() {
        final var c1 = new DirectoryNode(new Directory("/fs"));
        final var c2 = new DirectoryNode(new Directory("/fs/dir1"));

        // Build /fs/dir1
        c1.setParent(node);
        c2.setParent(c1);

        assertThrows(InvalidChildException.class, () -> node.setParent(c2));
        assertThrows(InvalidChildException.class, () -> node.setParent(c1));
        assertThrows(InvalidChildException.class, () -> c1.setParent(c2));
    }

    @Test
    @DisplayName("Setup invalid children and expect them to be rejected")
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
    @DisplayName("Build a simple file tree")
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
        final var fileX = new FileNode(new File.TextFile("root/dir1/file-x.txt"));
        final var fileY = new FileNode(new File.TextFile("root/dir2/file-y.txt"));

        dir1.addChild(fileX);
        dir2.addChild(fileY);

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

        assertThat(dir11.getParent(), is(Optional.of(dir1)));
        assertThat(dir1.getParent(), is(Optional.of(root)));
        assertThat(root.getParent(), is(Optional.empty()));

        assertTrue(root.hasChild(dir1.commonFile()), "Root has dir1");
        assertTrue(root.hasChild(dir2.commonFile()), "Root has dir2");
        assertTrue(root.hasChild(file1.commonFile()), "Root has file1");
        assertTrue(root.hasChild(file2.commonFile()), "Root has file2");
        assertTrue(dir1.hasChild(dir11.commonFile()), "Dir1 has dir11");
        assertTrue(dir1.hasChild(fileX.commonFile()), "Dir1 has file-x.txt");
        assertTrue(dir11.hasChild(nestedFile.commonFile()), "Dir1 has nested.txt");
        assertTrue(dir2.hasChild(fileY.commonFile()), "Dir2 has file-y.txt");

        // Test recursive traversal
        final String[] traverseExpected = {
            "root",
            "root/dir1",
            "root/dir1/file-x.txt",
            "root/dir1/dir11",
            "root/dir1/dir11/nested.txt",
            "root/dir2",
            "root/dir2/file-y.txt",
            "root/file1.txt",
            "root/file2.txt"
        };
        final var traverseResult = new ArrayList<>(traverseExpected.length);

        root.traverse(next -> traverseResult.add(next.toString()));

        assertThat("The traversed nodes are expected", traverseResult.toArray(), is(traverseExpected));
    }
}
