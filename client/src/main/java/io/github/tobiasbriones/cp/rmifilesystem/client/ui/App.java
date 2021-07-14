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

package io.github.tobiasbriones.cp.rmifilesystem.client.ui;

import io.github.tobiasbriones.cp.rmifilesystem.client.FileSystemServices;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.MvpView;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.content.Content;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.header.Header;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.menu.AppMenu;
import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Tobias Briones
 */
public final class App implements Initializable {
    private static final double MIN_WIDTH = 480.0d;
    private static final double MIN_HEIGHT = 600.0d;

    interface Presenter extends MvpPresenter<Void> {}

    interface View extends MvpView<Void> {}

    public static App newInstance() {
        final var menu = new AppMenu();
        final var header = new Header();
        final var childrenConfig = new ChildrenConfig(
            menu,
            header,
            Content.newInstance()
        );
        return new App(childrenConfig);
    }

    record ChildrenConfig(
        AppMenu menu,
        Header header,
        Content content
    ) {
        ViewConfig newViewConfig() {
            return new ViewConfig(
                menu.getView(),
                header.getView(),
                content.getView()
            );
        }
    }

    record ViewConfig(
        Node menuView,
        Node headerView,
        Node contentView
    ) {}

    private final View view;
    private final Presenter presenter;
    private final AppMenu menu;
    private final AppMenuOutput menuOutput;
    private final Header header;
    private final Content content;
    private FileSystemService service;

    private App(ChildrenConfig childrenConfig) {
        view = new AppView(childrenConfig.newViewConfig());
        menu = childrenConfig.menu();
        menuOutput = new AppMenuOutput(childrenConfig.header().getInput(), this::quit);
        header = childrenConfig.header();
        content = childrenConfig.content();
        presenter = new AppPresenter(view, header.getInput());
        service = null;
    }

    @Override
    public void init() {
        presenter.init();
        menu.init();
        header.init();
        content.init();
    }

    public void start(Stage stage) {
        final var scene = new Scene((Parent) view);
        final var title = "JavaRMI Text File System";

        menu.setOutput(menuOutput);
        init();

        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.show();

        stage.setOnCloseRequest(event -> exit());
        loadServiceAsync();
    }

    private void loadServiceAsync() {
        final Consumer<Optional<FileSystemService>> update = result ->
            result.ifPresentOrElse(this::onServiceObtained, this::onFailedToObtainService);

        final Runnable run = () -> {
            final var result = obtainService();

            Platform.runLater(() -> update.accept(result));
        };
        final var thread = new Thread(run);

        setRetrievingServiceStatus(header.getInput());
        thread.start();
    }

    private Optional<FileSystemService> obtainService() {
        try {
            return Optional.ofNullable(FileSystemServices.obtainService());
        }
        catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void onServiceObtained(FileSystemService value) {
        service = value;
        menuOutput.setService(service);
        content.setService(service);
        setServiceRetrievedStatus(header.getInput());
    }

    private void onFailedToObtainService() {
        setFailedServiceStatus(header.getInput());
    }

    private void quit() {
        exit();
        Platform.exit();
    }

    private void exit() {
        try {
            content.unbind();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static void setServiceRetrievedStatus(Header.Input input) {
        input.setStatus("Connected");
    }

    private static void setRetrievingServiceStatus(Header.Input input) {
        final var msg = "Retrieving service from %s...".formatted(FileSystemServices.HOST);
        input.setStatus(msg);
    }

    private static void setFailedServiceStatus(Header.Input input) {
        input.setStatus("Failed");
    }
}
