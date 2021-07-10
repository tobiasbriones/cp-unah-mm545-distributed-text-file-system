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

import static io.github.tobiasbriones.cp.rmifilesystem.model.io.node.Node.*;

/**
 * @author Tobias Briones
 */
final class Nodes {
    static void requireValidParent(DirectoryNode node, DirectoryNode parent) {

    }

    static void requireValidChild(DirectoryNode node, Node<?> child) {

    }

    private static boolean isValidChild(CommonPath path, CommonPath childPath) {
        final String[] tokens = path.split();
        final String[] childTokens = childPath.split();

        if (tokens.length != childTokens.length - 1) {
            return false;
        }
        return childPath.getParent().equals(path);
    }

    private Nodes() {}
}
