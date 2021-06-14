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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Tobias Briones
 */
final class EditorView extends VBox implements Editor.View {
    private static final int SAVE_BUTTON_WIDTH = 240;
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
        final var actionPane = new HBox();

        saveButton.setText("Save");
        saveButton.setPrefWidth(SAVE_BUTTON_WIDTH);

        actionPane.getChildren().addAll(saveButton);
        actionPane.setAlignment(Pos.CENTER_RIGHT);
        actionPane.setPadding(new Insets(8, 0, 8, 0));

        getChildren().addAll(
            contentArea,
            actionPane
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
