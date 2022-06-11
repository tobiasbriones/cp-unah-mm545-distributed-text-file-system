// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io.node;

import engineer.mathsoftware.cp.dtfs.io.CommonFile;
import engineer.mathsoftware.cp.dtfs.io.CommonPath;
import engineer.mathsoftware.cp.dtfs.io.Directory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Tobias Briones
 */
final class Nodes {
    private Nodes() {}

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

    static boolean removeChild(
        DirectoryNode node,
        Node<? extends CommonFile> child
    ) {
        if (!node.hasChild(child.commonFile())) {
            return false;
        }

        if (child instanceof DirectoryNode dir) {
            dir.setParentUnsafe(null);
        }
        return node.removeChildUnsafe(child);
    }

    static void traverse(
        Iterable<? extends Node<?>> children,
        Consumer<? super Node<?>> nodeConsumer
    ) {
        children.forEach(child -> {
            nodeConsumer.accept(child);

            if (child instanceof DirectoryNode dir) {
                traverse(dir.getChildren(), nodeConsumer);
            }
        });
    }

    static String getString(
        Node<? extends CommonFile> node,
        String indentation
    ) {
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

    private static void requireValidChild(DirectoryNode node, Node<?> child) {
        if (!isValidChild(node, child)) {
            throw new InvalidChildException(
                node.commonFile(),
                child.commonFile()
            );
        }
    }

    private static void addChildUnsafe(DirectoryNode node, Node<?> child) {
        node.addChildUnsafe(child);

        if (child instanceof DirectoryNode dir) {
            dir.setParentUnsafe(node);
        }
    }

    private static String toString(CommonFile file, String indentation) {
        return """
               %s%s
               """.formatted(indentation, file.name());
    }

    private static boolean isValidChild(DirectoryNode node, Node<?> child) {
        return !node.hasChild(child.commonFile()) && isValidChildPath(
            node.commonPath(),
            child.commonPath()
        );
    }

    private static boolean isValidChildPath(
        CommonPath path,
        CommonPath childPath
    ) {
        final String[] tokens = path.split();
        final String[] childTokens = childPath.split();

        if (tokens.length != childTokens.length - 1) {
            return false;
        }
        return childPath.getParent().equals(path);
    }
}
