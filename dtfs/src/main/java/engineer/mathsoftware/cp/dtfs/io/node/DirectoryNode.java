// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io.node;

import engineer.mathsoftware.cp.dtfs.io.CommonFile;
import engineer.mathsoftware.cp.dtfs.io.CommonPath;
import engineer.mathsoftware.cp.dtfs.io.Directory;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Tobias Briones
 */
public final class DirectoryNode implements Serializable,
                                            Node<Directory>,
                                            Iterable<Node<? extends CommonFile>> {

    @Serial
    private static final long serialVersionUID = 1041326646249311639L;
    private static final int INITIAL_CHILDREN_CAPACITY = 10;

    public static DirectoryNode of() {
        return new DirectoryNode();
    }

    private final Directory directory;
    private final List<Node<? extends CommonFile>> children;
    private DirectoryNode parent; // this one should be an
    // Optional<Directory> for future versions of Java

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
     * @throws InvalidChildException if the given parent for this node is
     *                               circular or invalid
     */
    public void setParent(DirectoryNode value) {
        Nodes.setParent(this, value);
    }

    public Collection<Node<? extends CommonFile>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    void setParentUnsafe(DirectoryNode node) {
        parent = node;
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
        return directory.path().value();
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public boolean hasChild(CommonFile file) {
        return children.stream()
                       .map(Node::commonFile)
                       .anyMatch(child -> child.path().equals(file.path()));
    }

    /**
     * Adds the given child to this directory node.
     *
     * @param child child node to add to this directory node
     *
     * @throws InvalidChildException if the given child is invalid
     */
    public void addChild(Node<? extends CommonFile> child) {
        Nodes.addChild(this, child);
    }

    /**
     * Adds the given children to this directory node. If one of the children is
     * invalid then nothing happens and always throws
     * {@link InvalidChildException}.
     *
     * @param values children nodes to add to this directory node
     *
     * @throws InvalidChildException if one of the given children is invalid
     */
    public void addChildren(Node<? extends CommonFile>... values) {
        Nodes.addChildren(this, values);
    }

    public boolean removeChild(Node<? extends CommonFile> child) {
        return Nodes.removeChild(this, child);
    }

    public void traverse(Consumer<? super Node<?>> nodeConsumer) {
        nodeConsumer.accept(this);
        Nodes.traverse(children, nodeConsumer);
    }

    void addChildUnsafe(Node<?> child) {
        children.add(child);
    }

    boolean removeChildUnsafe(Node<?> child) {
        return children.remove(child);
    }
}
