package io.github.tobiasbriones.cp.rmifilesystem.ui.header;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

final class HeaderView extends HBox implements Header.View {
    private final Label userLabel;

    HeaderView() {
        super();
        userLabel = new Label();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        getChildren().addAll(
            userLabel
        );
    }

    @Override
    public void setUser(String value) {
        userLabel.setText(value);
    }
}
