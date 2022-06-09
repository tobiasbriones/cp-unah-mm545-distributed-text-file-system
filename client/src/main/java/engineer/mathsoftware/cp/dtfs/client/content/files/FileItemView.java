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

package engineer.mathsoftware.cp.dtfs.client.content.files;

import engineer.mathsoftware.cp.dtfs.model.io.node.DirectoryNode;
import engineer.mathsoftware.cp.dtfs.model.io.node.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.Collection;

/**
 * @author Tobias Briones
 */
final class FileItemView extends TreeItem<Node<?>> {
    private boolean didLoadChildrenAlready;
    private boolean isFirstTimeLeaf;
    private boolean isLeaf;

    FileItemView(Node<?> node) {
        super(node);
        didLoadChildrenAlready = true;
        isFirstTimeLeaf = true;
    }

    @Override
    public boolean isLeaf() {
        if (isFirstTimeLeaf) {
            isFirstTimeLeaf = false;
            isLeaf = getValue().isFile();
        }
        return getValue().isFile();
    }

    @Override
    public ObservableList<TreeItem<Node<?>>> getChildren() {
        if (didLoadChildrenAlready) {
            didLoadChildrenAlready = false;
            super.getChildren().setAll(loadChildren(this));
        }
        return super.getChildren();
    }

    @Override
    public String toString() {
        return "FileItemView[] " + super.toString();
    }

    private static ObservableList<TreeItem<Node<?>>> loadChildren(TreeItem<? extends Node<?>> item) {
        final Node<?> node = item.getValue();

        if (node instanceof DirectoryNode dir) {
            final Collection<Node<?>> childrenNodes = dir.getChildren();

            if (!childrenNodes.isEmpty()) {
                final ObservableList<TreeItem<Node<?>>> children = FXCollections.observableArrayList();

                for (Node<?> child : childrenNodes) {
                    final var itemView = new FileItemView(child);

                    itemView.setExpanded(true);
                    children.add(itemView);
                }
                return children;
            }
        }
        return FXCollections.emptyObservableList();
    }
}
