// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client;

import engineer.mathsoftware.cp.dtfs.FileSystemService;
import engineer.mathsoftware.cp.dtfs.client.content.Content;
import engineer.mathsoftware.cp.dtfs.client.header.Header;
import engineer.mathsoftware.cp.dtfs.client.info.Info;
import engineer.mathsoftware.cp.dtfs.client.menu.AppMenu;
import engineer.mathsoftware.cp.dtfs.mvp.Initializable;
import engineer.mathsoftware.cp.dtfs.mvp.MvpPresenter;
import engineer.mathsoftware.cp.dtfs.mvp.MvpView;
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
class App implements Initializable {
    private static final double MIN_WIDTH = 480.0d;
    private static final double MIN_HEIGHT = 600.0d;

    interface Presenter extends MvpPresenter<Void> {}

    interface View extends MvpView<Void> {}

    static App newInstance() {
        var repository = AppLocalFiles.newTextFileRepository();
        var menu = new AppMenu();
        var header = new Header();
        var info = new Info();
        var childrenConfig = new ChildrenConfig(
            menu,
            header,
            Content.newInstance(new Content.DependencyConfig(
                repository,
                header.getInput(),
                info.getInput()
            )),
            info
        );
        return new App(childrenConfig);
    }

    private final View view;
    private final Presenter presenter;
    private final AppMenu menu;
    private final AppMenuOutput menuOutput;
    private final Header header;
    private final Content content;
    private final Info info;
    private FileSystemService service;

    private App(ChildrenConfig childrenConfig) {
        view = new AppView(childrenConfig.newViewConfig());
        menu = childrenConfig.menu();
        menuOutput = new AppMenuOutput(
            childrenConfig.header().getInput(),
            this::quit
        );
        header = childrenConfig.header();
        content = childrenConfig.content();
        info = childrenConfig.info();
        presenter = new AppPresenter(view, header.getInput());
        service = null;
    }

    @Override
    public void init() {
        presenter.init();
        menu.init();
        header.init();
        content.init();
        info.init();
    }

    public void start(Stage stage) {
        var scene = new Scene((Parent) view);
        var title = "JavaRMI Text File System";

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
        Consumer<Optional<FileSystemService>> update = result ->
            result.ifPresentOrElse(
                this::onServiceObtained,
                this::onFailedToObtainService
            );

        Runnable run = () -> {
            var result = obtainService();
            Platform.runLater(() -> update.accept(result));
        };
        var thread = new Thread(run);

        setRetrievingServiceStatus();
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

        setServiceRetrievedStatus();
        menuOutput.setService(service);
        content.setService(service);
    }

    private void onFailedToObtainService() {
        setFailedServiceStatus();
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

    private void setServiceRetrievedStatus() {
        header.getInput().setStatus("Connecting");
        info.getInput().end("");
    }

    private void setRetrievingServiceStatus() {
        var msg = "Retrieving service from %s...".formatted(FileSystemServices.HOST);
        header.getInput().setStatus(msg);
        info.getInput().start("Retrieving service");
    }

    private void setFailedServiceStatus() {
        header.getInput().setStatus("Failed");
        info.getInput().end("");
        info.getInput()
            .setError("Failed to connect to service: %s".formatted(FileSystemServices.HOST));
    }

    record ChildrenConfig(
        AppMenu menu,
        Header header,
        Content content,
        Info info
    ) {
        ViewConfig newViewConfig() {
            return new ViewConfig(
                menu.getView(),
                header.getView(),
                content.getView(),
                info.getView()
            );
        }
    }

    record ViewConfig(
        Node menuView,
        Node headerView,
        Node contentView,
        Node infoView
    ) {}
}
