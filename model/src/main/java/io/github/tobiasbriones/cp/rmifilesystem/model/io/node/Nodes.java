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

import io.github.tobiasbriones.cp.rmifilesystem.model.io.CommonFile;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.CommonPath;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tobias Briones
 */
final class Nodes {
    static void setParent(Node<Directory> node, DirectoryNode parent) {
        addChild(parent, node);
    }

    static void addChild(DirectoryNode node, Node<?> child) {
        requireValidChild(node, child);
        addChildUnsafe(node, child);
    }

    static void addChildren(DirectoryNode node, Node<?>... children) {
        final List<Node<? extends CommonFile>> list = Arrays.asList(children);

        list.forEach(child -> requireValidChild(node, child));
        list.forEach(child -> addChildUnsafe(node, child));
    }

    static boolean removeChild(DirectoryNode node, Node<? extends CommonFile> child) {
        if (!node.hasChild(child.commonFile())) {
            return false;
        }

        if (child instanceof DirectoryNode dir) {
            dir.setParentUnsafe(null);
        }
        return node.removeChildUnsafe(child);
    }

    private static void requireValidChild(DirectoryNode node, Node<?> child) {
        if (!isValidChild(node, child)) {
            throw new InvalidChildException(node.commonFile(), child.commonFile());
        }
    }

    private static void addChildUnsafe(DirectoryNode node, Node<?> child) {
        node.addChildUnsafe(child);

        if (child instanceof DirectoryNode dir) {
            dir.setParentUnsafe(node);
        }
    }

    static String getString(Node<? extends CommonFile> node, String indentation) {
        final CommonFile file = node.commonFile();

        if (node instanceof DirectoryNode dir && dir.hasChildren()) {
            final var sb = new StringBuilder(10);

            sb.append(toString(file, indentation));
            for (final var child : dir) {
                final String str = getString(child, indentation + "  ");

                sb.append(str);
            }
            return sb.toString();
        }
        return toString(file, indentation);
    }

    private static String toString(CommonFile file, String indentation) {
        return """
               %s%s
               """.formatted(indentation, file.name());
    }

    private static boolean isValidChild(DirectoryNode node, Node<?> child) {
        return !node.hasChild(child.commonFile()) && isValidChildPath(node.commonPath(), child.commonPath());
    }

    private static boolean isValidChildPath(CommonPath path, CommonPath childPath) {
        final String[] tokens = path.split();
        final String[] childTokens = childPath.split();

        if (tokens.length != childTokens.length - 1) {
            return false;
        }
        return childPath.getParent().equals(path);
    }

    private Nodes() {}
}
