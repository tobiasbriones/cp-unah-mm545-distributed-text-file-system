// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.content;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * @author Tobias Briones
 */
class ContentView extends HBox implements Content.View {
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
