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

package io.github.tobiasbriones.cp.rmifilesystem.client.content;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * @author Tobias Briones
 */
final class ContentView extends HBox implements Content.View {
    private static final int SPACING = 16;
    private final Node filesView;
    private final Node editorView;

    ContentView(Content.ViewConfig config) {
        super();
        filesView = config.filesView();
        editorView = config.editorView();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        HBox.setHgrow(filesView, Priority.ALWAYS);
        HBox.setHgrow(editorView, Priority.ALWAYS);
        setSpacing(SPACING);
        getChildren().addAll(
            filesView,
            editorView
        );
    }
}
