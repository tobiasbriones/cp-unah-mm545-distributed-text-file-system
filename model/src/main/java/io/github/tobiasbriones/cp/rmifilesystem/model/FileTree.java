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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

/**
 * @author Tobias Briones
 */
public final class FileTree<T extends ClientFile> implements Iterable<FileTree.Node<T>> {
    private final Node<T> root;

    public FileTree(T rootFile) {
        root = new Node<>(rootFile);
    }

    public Node<T> getRoot() {
        return root;
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return root.iterator();
    }

    @Override
    public String toString() {
        return "root=" + root;
    }

    public static final class Node<T extends ClientFile> implements Iterable<Node<T>> {
        private static final int INITIAL_CAPACITY = 10;
        private final T file;
        private final List<Node<T>> children;

        public Node(T file) {
            this.file = file;
            this.children = new ArrayList<>(INITIAL_CAPACITY);
        }

        public String getName() {
            return file.getName();
        }

        @Override
        public String toString() {
            @FunctionalInterface
            interface Concatenetor extends BinaryOperator<String> {}
            final Concatenetor concatenator = (s1, s2) -> """
                                                          %s
                                                          %s
                                                          """.formatted(s1, s2)
                                                             .trim();
            return children.stream()
                           .map(Node::getName)
                           .reduce("", concatenator);
        }

        @Override
        public Iterator<Node<T>> iterator() {
            return children.iterator();
        }

        @SafeVarargs
        public final boolean addChildren(Node<T>... values) {
            return children.addAll(Arrays.asList(values));
        }

        public boolean hasChildren() {
            return !children.isEmpty();
        }

        public boolean addChild(Node<T> child) {
            return children.add(child);
        }

        public void clear() {
            children.clear();
        }

        public String toRecursiveString() {
            return getString(this, "");
        }

        private static String getString(Node<?> node, String indentation) {
            @FunctionalInterface
            interface ToStringSupplier extends Supplier<String> {}
            final ToStringSupplier supplier = () ->
                """
                %s%s
                """.formatted(
                    indentation,
                    node.getName()
                );

            if (node.hasChildren()) {
                final var sb = new StringBuilder(10);

                sb.append(supplier.get());
                for (var child : node) {
                    final var str = getString(child, indentation + "  ");

                    sb.append(str);
                }
                return sb.toString();
            }
            return supplier.get();
        }
    }
}














