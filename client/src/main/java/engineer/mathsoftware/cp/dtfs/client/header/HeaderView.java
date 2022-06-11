// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.header;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * @author Tobias Briones
 */
final class HeaderView extends VBox implements Header.View {
    private final Label userLabel;
    private final Label statusLabel;
    private final Label hostLabel;

    HeaderView() {
        super();
        userLabel = new Label();
        statusLabel = new Label();
        hostLabel = new Label();
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
        final var hostTitleLabel = new Label();

        userTitleLabel.setText("User: ");
        userTitleLabel.setStyle("-fx-font-weight: bold;");
        userBox.getChildren().addAll(userTitleLabel, userLabel);

        statusTitleLabel.setText("Service status: ");
        statusTitleLabel.setStyle("-fx-font-weight: bold;");

        hostTitleLabel.setText("Host: ");
        hostTitleLabel.setPadding(new Insets(0, 0, 0, 16));
        hostTitleLabel.setStyle("-fx-font-weight: bold;");
        hostLabel.setText("-");

        statusBox.setSpacing(4);
        statusBox.getChildren().addAll(statusTitleLabel,
                                       statusLabel,
                                       hostTitleLabel,
                                       hostLabel
        );

        setPadding(new Insets(8, 0, 8, 0));
        setSpacing(8);
        getChildren().addAll(userBox, statusBox);
    }

    @Override
    public void setUser(String value) {
        userLabel.setText(value);
    }

    @Override
    public void setConnected(String host) {
        loadIcon("ic_status_connected.png").ifPresent(statusLabel::setGraphic);
        setStatus("Connected");
        setHost(host);
    }

    @Override
    public void setStatus(String value) {
        statusLabel.setText(value);
    }

    @Override
    public void setHost(String value) {
        hostLabel.setText(value);
    }

    private Optional<ImageView> loadIcon(String iconName) {
        Optional<ImageView> image = Optional.empty();
        final var path = "/" + iconName;

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                image = Optional.of(new ImageView(new Image((is))));
            }
        }
        catch (IOException ignore) {
        }
        return image;
    }
}
