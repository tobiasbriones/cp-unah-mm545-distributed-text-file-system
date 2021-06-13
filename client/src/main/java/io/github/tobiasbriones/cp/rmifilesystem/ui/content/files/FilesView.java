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

package io.github.tobiasbriones.cp.rmifilesystem.ui.content.files;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.File;

final class FilesView extends VBox implements Files.View {
    private static final int MIN_WIDTH = 240;
    private static final int MAX_WIDTH = 360;
    private Files.Controller controller;

    FilesView() {
        super();
        controller = null;
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        setMinWidth(MIN_WIDTH);
        setMaxWidth(MAX_WIDTH);
    }

    @Override
    public void setController(Files.Controller value) {
        controller = value;
    }

    @Override
    public void addItem(File file) {
        final var item = new FileItemView();

        item.setName(file.getName());
        getChildren().add(item);

        if (controller != null) {
            item.setOnMouseClicked(event -> controller.onItemClick(file));
        }
    }

    @Override
    public void clear() {
        getChildren().clear();
        // Might need to unregister the events
    }
}
