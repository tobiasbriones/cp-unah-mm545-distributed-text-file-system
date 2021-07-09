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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    private Node node;

    NodeTest() {
        node = null;
    }

    @BeforeEach
    void setUp() {
        node = new Node(Directory.of());
    }

    @Test
    void isRoot() {
    }

    @Test
    void isDirectory() {
    }

    @Test
    void isFile() {
    }

    @Test
    void getParent() {
    }

    @Test
    void setParent() {
    }

    @Test
    void getChildren() {
    }

    @Test
    void addChild() {
    }

    @Test
    void commonFile() {
    }

    @Test
    void toJavaFile() {
    }

    @Test
    void hasChild() {
    }
}
