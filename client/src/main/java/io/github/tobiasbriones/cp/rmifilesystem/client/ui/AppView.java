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

package io.github.tobiasbriones.cp.rmifilesystem.client.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * @author Tobias Briones
 */
final class AppView extends VBox implements App.View {
    private static final int PADDING = 16;
    private final Node menu;
    private final Node header;
    private final Node content;

    AppView(App.ViewConfig config) {
        super();
        menu = config.menuView();
        header = config.headerView();
        content = config.contentView();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        final var container = new VBox();

        container.setPadding(new Insets(PADDING));
        container.getChildren().addAll(
            header,
            content
        );
        getChildren().addAll(
            menu,
            container
        );
    }
}
