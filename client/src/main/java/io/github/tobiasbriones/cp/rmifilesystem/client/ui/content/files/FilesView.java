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

package io.github.tobiasbriones.cp.rmifilesystem.client.ui.content.files;

import io.github.tobiasbriones.cp.rmifilesystem.model.ClientFile;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 * @author Tobias Briones
 */
final class FilesView extends VBox implements Files.View {
    private static final int MIN_WIDTH = 240;
    private static final int MAX_WIDTH = 360;
    private final TextField newFileField;
    private final Button newFileButton;
    private final VBox filesPane;
    private Files.Controller controller;

    FilesView() {
        super();
        newFileField = new TextField();
        newFileButton = new Button();
        controller = null;
        filesPane = new VBox();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        final var newFilePane = new VBox();

        newFileField.setPromptText("Create a new file or directory");
        newFileButton.setText("Create");
        newFileButton.setMaxWidth(MAX_WIDTH);

        newFilePane.getChildren().addAll(
            new Label("Files"),
            newFileField,
            newFileButton
        );
        newFilePane.setSpacing(8);
        HBox.setHgrow(newFilePane, Priority.ALWAYS);

        getChildren().addAll(
            newFilePane,
            filesPane
        );
        setMinWidth(MIN_WIDTH);
        setMaxWidth(MAX_WIDTH);
        setSpacing(8);
    }

    @Override
    public void setController(Files.Controller value) {
        controller = value;
        newFileButton.setOnMouseClicked(event -> controller.onCreateButtonClick());
    }

    @Override
    public String getCreateInputText() {
        return newFileField.getText();
    }

    @Override
    public void addItem(File file) {
        final var item = new FileItemView();

        item.set(new ClientFile(file));
        filesPane.getChildren().add(item);

        if (controller != null) {
            item.setOnMouseClicked(event -> controller.onItemClick(file));
        }
    }

    @Override
    public void clear() {
        filesPane.getChildren().clear();
        // Might need to unregister the events
    }
}
