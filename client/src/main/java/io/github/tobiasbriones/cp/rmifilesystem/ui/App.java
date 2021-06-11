package io.github.tobiasbriones.cp.rmifilesystem.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class App {
    public static App newInstance() {
        final var root = new AppView();
        return new App(root);
    }

    private final Parent root;

    private App(Parent root) {
        this.root = root;
    }

    public void start(Stage stage) {
        final var scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
}
