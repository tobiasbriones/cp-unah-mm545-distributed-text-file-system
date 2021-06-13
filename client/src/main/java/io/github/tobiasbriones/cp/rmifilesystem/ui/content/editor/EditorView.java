package io.github.tobiasbriones.cp.rmifilesystem.ui.content.editor;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

final class EditorView extends VBox implements Editor.View {
    private final TextArea contentArea;
    private final Button saveButton;

    EditorView() {
        super();
        contentArea = new TextArea();
        saveButton = new Button();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        saveButton.setText("Save");

        getChildren().addAll(
            contentArea,
            new HBox(saveButton)
        );
    }

    @Override
    public void setContent(String value) {
        contentArea.setText(value);
    }
}
