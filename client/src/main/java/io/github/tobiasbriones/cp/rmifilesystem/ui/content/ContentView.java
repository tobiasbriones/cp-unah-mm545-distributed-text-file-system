package io.github.tobiasbriones.cp.rmifilesystem.ui.content;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

final class ContentView extends HBox implements Content.View {
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
        getChildren().addAll(
            filesView,
            editorView
        );
    }
}
