package io.github.tobiasbriones.cp.rmifilesystem.ui.content.files;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

final class FilesView extends VBox implements Files.View {

    FilesView() {
        super();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        // TODO
    }
}
