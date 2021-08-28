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

package com.github.tobiasbriones.cp.rmifilesystem.client.content.files;

import com.github.tobiasbriones.cp.rmifilesystem.model.io.CommonFile;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.node.DirectoryNode;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.node.Node;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author Tobias Briones
 */
final class FilesView extends VBox implements Files.View {
    private static final int MIN_WIDTH = 240;
    private static final int MAX_WIDTH = 360;
    private final TextField newFileField;
    private final Button newFileButton;
    private final VBox filesPane;
    private final TreeView<Node<?>> treeView;
    private Files.Controller controller;

    FilesView() {
        super();
        newFileField = new TextField();
        newFileButton = new Button();
        filesPane = new VBox();
        treeView = new TreeView<>();
        controller = null;
    }

    @Override
    public javafx.scene.Node getView() {
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

        filesPane.getChildren().add(treeView);

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
        treeView.setCellFactory(p -> new TreeCellFactory(controller));
    }

    @Override
    public String getCreateInputText() {
        return newFileField.getText();
    }

    @Override
    public void setCreateInputText(String text) {
        newFileField.setText(text);
    }

    @Override
    public void setRoot(DirectoryNode root) {
        final var rootItem = new FileItemView(root);

        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);
        final ChangeListener<? super TreeItem<Node<?>>> l =  (
            observable, oldValue,
            newValue
        ) -> {
            if (newValue != null) {
                final Node<?> value = newValue.getValue();

                controller.onItemClick(value.commonFile());
            }
        };

        treeView.getSelectionModel().selectedItemProperty().addListener(l);
    }

    @Override
    public void clear() {
        treeView.setRoot(null);
    }

    private static final class TreeCellFactory extends TreeCell<Node<?>> {
        private static final String TEXT_FILE_ICON_NAME = "ic_text_file.png";
        private static final String FOLDER_ICON_NAME = "ic_folder.png";
        private final Files.Controller controller;

        TreeCellFactory(Files.Controller controller) {
            super();
            this.controller = controller;
        }

        @Override
        public void updateItem(Node<?> item, boolean empty) {
            super.updateItem(item, empty);
            var styles = "";

            if (empty) {
                setStyle("");
                setText("");
                setGraphic(null);
            }
            else {
                final CommonFile file = item.commonFile();

                setText(file);
                setupContextMenu(item);
                setupIconView(file);

                if (file instanceof File f) {
                    final boolean isInvalid = controller.getStatus(f).isInvalid();
                    final boolean isInChangelist = controller.isInChangelist(f);

                    if (isInvalid) {
                        if (isInChangelist) {
                            styles += "-fx-text-fill: #E64A19;"; // red
                        }
                        else {
                            styles += "-fx-text-fill: #757575;"; // grey
                        }
                    }
                    else {
                        if (isInChangelist) {
                            styles += "-fx-text-fill: #1976D2;"; // blue
                        }
                    }
                }
            }

            if (isSelected()) {
                styles += "-fx-background-color: #64B5F6;";
            }
            else {
                styles += "-fx-background-color: none;";
            }
            setStyle(styles);
        }

        void setText(CommonFile file) {
            if (file instanceof File f) {
                setText(f.fileName().value());
            }
            else {
                setText(file.name());
            }
        }

        void setupContextMenu(Node<?> item) {
            final ContextMenu menu = new ContextMenu();
            final var deleteItem = new MenuItem("Delete");

            if(item instanceof DirectoryNode dir) {
                final var newFileItem = new MenuItem("New file");
                final var newDirItem = new MenuItem("New directory");

                menu.getItems().addAll(newFileItem, newDirItem);

                newFileItem.setOnAction(event -> controller.onNewFileAction(dir));
                newDirItem.setOnAction(event -> controller.onNewDirectoryAction(dir));
            }
            menu.getItems().addAll(deleteItem);

            deleteItem.setOnAction(event -> controller.onDeleteAction(item));
            setContextMenu(menu);
        }

        void setupIconView(CommonFile file) {
            final String iconName = (file instanceof File.TextFile) ? TEXT_FILE_ICON_NAME : FOLDER_ICON_NAME;

            setupIconView(iconName);
        }

        void setupIconView(String value) {
            final var iconView = new ImageView();

            loadIcon(value).ifPresent(iconView::setImage);
            setGraphic(iconView);
        }

        Optional<Image> loadIcon(String iconName) {
            Optional<Image> image = Optional.empty();
            final var path = "/" + iconName;

            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is != null) {
                    image = Optional.of(new Image((is)));
                }
            }
            catch (IOException ignore) {}
            return image;
        }
    }
}
