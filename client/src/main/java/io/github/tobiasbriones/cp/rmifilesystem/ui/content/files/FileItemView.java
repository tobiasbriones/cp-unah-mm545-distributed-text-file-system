package io.github.tobiasbriones.cp.rmifilesystem.ui.content.files;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

final class FileItemView extends HBox {
    private final Label label;

    FileItemView() {
        super();
        label = new Label();

        init();
    }

    void setName(String name) {
        label.setText(name);
    }

    private void init() {
        setPadding(new Insets(8));
        getChildren().addAll(label);
    }
}
