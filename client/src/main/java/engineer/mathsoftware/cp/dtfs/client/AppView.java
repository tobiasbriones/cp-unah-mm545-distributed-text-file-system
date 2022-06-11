// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client;

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
    private final Node info;

    AppView(App.ViewConfig config) {
        super();
        menu = config.menuView();
        header = config.headerView();
        content = config.contentView();
        info = config.infoView();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        var container = new VBox();

        container.setPadding(new Insets(PADDING));
        container.getChildren().addAll(
            header,
            content
        );
        getChildren().addAll(
            menu,
            container,
            info
        );
    }
}
