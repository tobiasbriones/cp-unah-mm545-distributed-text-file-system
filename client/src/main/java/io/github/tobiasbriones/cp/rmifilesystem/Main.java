package io.github.tobiasbriones.cp.rmifilesystem;

import io.github.tobiasbriones.cp.rmifilesystem.ui.App;
import javafx.application.Application;
import javafx.stage.Stage;

public final class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private final App app;

    public Main() {
        super();
        app = App.newInstance();
    }

    @Override
    public void start(Stage primaryStage) {
        app.start(primaryStage);
    }
}
