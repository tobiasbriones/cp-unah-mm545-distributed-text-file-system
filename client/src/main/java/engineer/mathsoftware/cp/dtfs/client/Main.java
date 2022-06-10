// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Tobias Briones
 */
public final class Main extends Application {
    public static void main(String[] args) {
        if (args.length > 0) {
            final var hostname = args[0];

            System.setProperty("java.rmi.server.hostname", hostname);
        }
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
