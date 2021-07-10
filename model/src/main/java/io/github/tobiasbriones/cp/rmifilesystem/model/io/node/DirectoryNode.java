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

import java.util.*;

/**
 * @author Tobias Briones
 */
public final class DirectoryNode implements Node<Directory>, Iterable<Node<? extends CommonFile>> {
    private static final int INITIAL_CHILDREN_CAPACITY = 10;
    private final Directory directory;
    private final List<Node<? extends CommonFile>> children;
    private DirectoryNode parent; // this one should be an Optional<Directory> for future versions of Java

    public DirectoryNode() {
        this(Directory.of());
    }

    public DirectoryNode(Directory directory) {
        this.directory = directory;
        children = new ArrayList<>(INITIAL_CHILDREN_CAPACITY);
        parent = null;
    }

    public boolean isRoot() {
        return directory.isRoot();
    }

    public Optional<DirectoryNode> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Sets this node's parent directory.
     *
     * @param value parent directory
     *
     * @throws CircularParentException if the given parent for this node is
     *                                 circular
     */
    public void setParent(DirectoryNode value) {
        Nodes.requireValidParent(this, value);
        parent = value;
    }

    public Collection<Node<? extends CommonFile>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public Directory commonFile() {
        return directory;
    }

    @Override
    public CommonPath commonPath() {
        return directory.path();
    }

    @Override
    public Iterator<Node<? extends CommonFile>> iterator() {
        return children.iterator();
    }

    @Override
    public String toString() {
        return directory.name();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public void addChild(Node<? extends CommonFile> child) {
        Nodes.requireValidChild(this, child);
        children.add(child);
    }

    public void addChildren(Node<? extends CommonFile>... values) {
        final List<Node<? extends CommonFile>> list = Arrays.asList(values);

        list.forEach(child -> Nodes.requireValidChild(this, child));
        children.addAll(list);
    }

    public boolean hasChild(CommonFile file) {
        return children.stream()
                       .map(Node::commonFile)
                       .anyMatch(child -> child.path().equals(file.path()));
    }
}
