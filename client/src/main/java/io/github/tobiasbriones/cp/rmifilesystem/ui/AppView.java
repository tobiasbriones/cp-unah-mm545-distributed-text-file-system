package io.github.tobiasbriones.cp.rmifilesystem.ui;

import javafx.scene.Node;
import javafx.scene.layout.VBox;

final class AppView extends VBox implements App.View {
    private final Node menu;

    AppView(App.ViewConfig config) {
        super();
        menu = config.menuView();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        getChildren().addAll(
            menu
        );
    }
}
