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

class ClientFileTest {
    private static final File DEF_FILE = new File("some_file.txt");
    private ClientFile file;

    ClientFileTest() {
        file = null;
    }

    @BeforeEach
    void setUp() {
        file = new LocalClientFile(DEF_FILE);
    }

    @Test
    @DisplayName("Gets the right file name")
    void testFileName() {
        assertThat(file.getName(), is("some_file.txt"));
    }

    @Test
    @DisplayName("Computes the right file extension")
    void testFileExtension() {
        assertThat(file.getExtension(), is("txt"));
    }
}
