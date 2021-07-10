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

import io.github.tobiasbriones.cp.rmifilesystem.model.io.CommonPath;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
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
        final var dirNode = new DirectoryNode(new Directory(new CommonPath("fs")));

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
        final var dirNode = new DirectoryNode(new Directory(new CommonPath("fs")));

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
        final var c1 = new DirectoryNode(new Directory(new CommonPath("fs")));
        final var c2 = new FileNode(new File.TextFile(new CommonPath("file1.txt")));

        node.addChild(c1);
        assertTrue(node.hasChild(c1.commonFile()), "A directory node is added as child");

        node.addChild(c2);
        assertTrue(node.hasChild(c2.commonFile()), "A file node is added as child");
    }

    @Test
    void addChildren() {
        final var c1 = new DirectoryNode(new Directory(new CommonPath("fs")));
        final var c2 = new FileNode(new File.TextFile(new CommonPath("file1.txt")));
        final var c3 = new FileNode(new File.TextFile(new CommonPath("file2.txt")));

        node.addChildren(c1, c2, c3);

        assertThat(
            "The default node has children when adding them as children",
            node.getChildren(),
            containsInAnyOrder(c1, c2, c3)
        );

        assertThat(
            "Directory child's parent is the default node",
            c1.getParent(),
            is(Optional.of(node))
        );
    }

    @Test
    void hasChild() {
        final var c1 = new DirectoryNode(new Directory(new CommonPath("fs")));
        final var c2 = new FileNode(new File.TextFile(new CommonPath("file1.txt")));

        node.addChildren(c1, c2);
        assertTrue(node.hasChild(c1.commonFile()), "Node has directory child after adding it");
        assertTrue(node.hasChild(c2.commonFile()), "Node has file child after adding it");
    }

    @Test
    void testCircularParent() {

    }

    @Test
    void testInvalidChild() {

    }

    @Test
    void testSampleFs() {

    }
}
