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

package io.github.tobiasbriones.cp.rmifilesystem.ui.content.editor;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Tobias Briones
 */
final class EditorView extends VBox implements Editor.View {
    private final TextArea contentArea;
    private final Button saveButton;

    EditorView() {
        super();
        contentArea = new TextArea();
        saveButton = new Button();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        saveButton.setText("Save");

        getChildren().addAll(
            contentArea,
            new HBox(saveButton)
        );
    }

    @Override
    public void bindEvents(Editor.Controller controller) {
        saveButton.setOnMouseClicked(event -> controller.onSaveButtonClick());
    }

    @Override
    public String getContent() {
        return contentArea.getText();
    }

    @Override
    public void setContent(String value) {
        contentArea.setText(value);
    }
}
