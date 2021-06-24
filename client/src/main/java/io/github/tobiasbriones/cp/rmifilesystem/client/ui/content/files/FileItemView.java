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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Tobias Briones
 */
final class FileItemView extends HBox {
    private static final String TEXT_FILE_ICON_NAME = "ic_text_file.png";
    private static final String FOLDER_ICON_NAME = "ic_folder.png";
    private final Label label;
    private final ImageView iconView;

    FileItemView() {
        super();
        label = new Label();
        iconView = new ImageView();

        init();
    }

    private void setName(String value) {
        label.setText(value);
    }

    private void setIcon(String value) {
        loadIcon(value).ifPresent(iconView::setImage);
    }

    void set(ClientFile file) {
        final Function<String, Boolean> isTextFile = name -> name.endsWith(".txt");
        final var name = file.getFile().getName();
        final var iconName = isTextFile.apply(
            file.getFile().getName()
        ) ? TEXT_FILE_ICON_NAME : FOLDER_ICON_NAME;

        setName(name);
        setIcon(iconName);
    }

    private void init() {
        setPadding(new Insets(8));
        setSpacing(8);
        getChildren().addAll(iconView, label);
        setAlignment(Pos.CENTER_LEFT);

        setOnMouseEntered(event -> setStyle("-fx-background-color: #E0E0E0"));
        setOnMouseExited(event -> setStyle("-fx-background-color: none"));
    }

    private Optional<Image> loadIcon(String iconName) {
        Optional<Image> image = Optional.empty();
        final var path = "/" + iconName;

        try (var is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                image = Optional.of(new Image((is)));
            }
        }
        catch (IOException ignore) {}
        return image;
    }
}
