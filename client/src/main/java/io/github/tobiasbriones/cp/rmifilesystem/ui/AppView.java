package io.github.tobiasbriones.cp.rmifilesystem.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

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
