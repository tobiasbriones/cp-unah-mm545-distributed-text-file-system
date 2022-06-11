// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.menu;

import engineer.mathsoftware.cp.dtfs.mvp.AbstractMvpPresenter;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

/**
 * @author Tobias Briones
 */
class AppMenuPresenter extends AbstractMvpPresenter<AppMenu.Output> implements AppMenu.Presenter {
    private final AppMenu.View view;

    AppMenuPresenter(AppMenu.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.createView();
        view.bindEvents(this);
    }

    @Override
    public void onSave() {
        getOutput().ifPresent(AppMenu.DefaultOutput::onSave);
    }

    @Override
    public void onClose() {
        getOutput().ifPresent(AppMenu.DefaultOutput::onClose);
    }

    @Override
    public void onNewFile() {
        // TODO
    }

    @Override
    public void onNewDirectory() {
        // TODO
    }

    @Override
    public void onLogin() {
        if (getOutput().isEmpty()) {
            return;
        }
        var dialog = new TextInputDialog("main");

        dialog.setHeaderText("Login as");
        dialog.showAndWait();

        var clientName = dialog.getEditor().getText();
        getOutput().ifPresent(output -> output.onLogin(clientName));
    }

    @Override
    public void onQuit() {
        getOutput().ifPresent(AppMenu.Output::onQuit);
    }

    @Override
    public void onAbout() {
        var alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setHeaderText("About");
        alert.setContentText(
            "GitHub: https://github"
            + ".com/tobiasbriones/cp-unah-mm545-distributed-text-file-system");
        alert.show();
    }
}
