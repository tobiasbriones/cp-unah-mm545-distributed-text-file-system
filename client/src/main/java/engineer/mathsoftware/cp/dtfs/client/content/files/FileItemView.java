// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.content.files;

import engineer.mathsoftware.cp.dtfs.io.node.DirectoryNode;
import engineer.mathsoftware.cp.dtfs.io.node.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/**
 * @author Tobias Briones
 */
class FileItemView extends TreeItem<Node<?>> {
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

    private static ObservableList<TreeItem<Node<?>>> loadChildren(
        TreeItem<? extends Node<?>> item
    ) {
        var node = item.getValue();

        if (node instanceof DirectoryNode dir) {
            var childrenNodes = dir.getChildren();

            if (!childrenNodes.isEmpty()) {
                var children = FXCollections.<TreeItem<Node<?>>>observableArrayList();

                for (var child : childrenNodes) {
                    var itemView = new FileItemView(child);
                    itemView.setExpanded(true);
                    children.add(itemView);
                }
                return children;
            }
        }
        return FXCollections.emptyObservableList();
    }
}
