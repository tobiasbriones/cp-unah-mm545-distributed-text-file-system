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

package io.github.tobiasbriones.cp.rmifilesystem.client.header;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Tobias Briones
 */
final class HeaderView extends VBox implements Header.View {
    private final Label userLabel;
    private final Label statusLabel;

    HeaderView() {
        super();
        userLabel = new Label();
        statusLabel = new Label();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        final var userBox = new HBox();
        final var userTitleLabel = new Label();
        final var statusBox = new HBox();
        final var statusTitleLabel = new Label();

        userTitleLabel.setText("User: ");
        userTitleLabel.setStyle("-fx-font-weight: bold;");
        userBox.getChildren().addAll(userTitleLabel, userLabel);

        statusTitleLabel.setText("Service status: ");
        statusTitleLabel.setStyle("-fx-font-weight: bold;");
        statusBox.getChildren().addAll(statusTitleLabel, statusLabel);

        setPadding(new Insets(8, 0, 8, 0));
        setSpacing(8);
        getChildren().addAll(userBox, statusBox);
    }

    @Override
    public void setUser(String value) {
        userLabel.setText(value);
    }

    @Override
    public void setStatus(String value) {
        statusLabel.setText(value);
    }
}
