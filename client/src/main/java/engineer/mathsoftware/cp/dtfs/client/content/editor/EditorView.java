// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.content.editor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author Tobias Briones
 */
class EditorView extends VBox implements Editor.View {
    private static final int SAVE_BUTTON_WIDTH = 240;
    private final Label fileLabel;
    private final TextArea contentArea;
    private final Button saveButton;
    private final Button pushButton;
    private final Button pullButton;

    EditorView() {
        super();
        fileLabel = new Label();
        contentArea = new TextArea();
        saveButton = new Button();
        pushButton = new Button();
        pullButton = new Button();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void bindEvents(Editor.Controller controller) {
        saveButton.setOnMouseClicked(event -> controller.onSaveButtonClick());
        pushButton.setOnMouseClicked(event -> controller.onPushButtonClick());
        pullButton.setOnMouseClicked(event -> controller.onPullButtonClick());
    }

    @Override
    public void createView() {
        var actionPane = new VBox();
        var servicePane = new HBox();

        fileLabel.setPadding(new Insets(0, 0, 8, 0));
        fileLabel.setStyle("-fx-font-weight: bold;-fx-font-size:16px;");
        saveButton.setText("Save");
        saveButton.setPrefWidth(SAVE_BUTTON_WIDTH);

        pushButton.setText("Push");
        pushButton.setGraphic(new ImageView(new Image("/ic_push.png")));
        pushButton.setPrefWidth(SAVE_BUTTON_WIDTH / 2 - 4);

        pullButton.setText("Pull");
        pullButton.setGraphic(new ImageView(new Image("/ic_pull.png")));
        pullButton.setPrefWidth(SAVE_BUTTON_WIDTH / 2 - 4);

        servicePane.getChildren().addAll(pushButton, pullButton);
        servicePane.setAlignment(Pos.CENTER_RIGHT);
        servicePane.setPadding(new Insets(8, 0, 8, 0));
        servicePane.setSpacing(8);

        actionPane.getChildren().addAll(saveButton, servicePane);
        actionPane.setAlignment(Pos.CENTER_RIGHT);
        actionPane.setPadding(new Insets(8, 0, 8, 0));

        VBox.setVgrow(contentArea, Priority.ALWAYS);
        getChildren().addAll(
            fileLabel,
            contentArea,
            actionPane
        );
    }

    @Override
    public String getContent() {
        return contentArea.getText();
    }

    @Override
    public void setContent(String value) {
        contentArea.setText(value);
    }

    @Override
    public void setWorkingFile(String fileName) {
        fileLabel.setText(fileName);
    }
}
