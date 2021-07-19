/*
 * Copyright (c) 2021 Tobias Briones. All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * This file is part of Course Project at UNAH-MM545: Distributed Text File
 * System.
 *
 * This source code is licensed under the BSD-3-Clause License found in the
 * LICENSE file in the root directory of this source tree or at
 * https://opensource.org/licenses/BSD-3-Clause.
 */

package io.github.tobiasbriones.cp.rmifilesystem.client;

import io.github.tobiasbriones.cp.rmifilesystem.model.AppProperties;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Tobias Briones
 */
public final class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private final App app;

    public Main() {
        super();
        app = App.newInstance();

        System.setProperty(
            "java.rmi.server.hostname",
            AppProperties.readHostname(getClass().getClassLoader())
        );
    }

    @Override
    public void start(Stage primaryStage) {
        app.start(primaryStage);
    }
}
