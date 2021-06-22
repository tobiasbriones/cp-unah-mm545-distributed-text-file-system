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

package io.github.tobiasbriones.cp.rmifilesystem.client.ui.menu;

import io.github.tobiasbriones.cp.rmifilesystem.client.ui.core.AbstractMvpPresenter;
import javafx.application.Platform;
import javafx.scene.control.*;

/**
 * @author Tobias Briones
 */
final class AppMenuPresenter extends AbstractMvpPresenter<AppMenu.Output> implements AppMenu.Presenter {
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
    public void onLogin() {
        if (getOutput().isEmpty()) {
            return;
        }
        final var dialog = new TextInputDialog("main");

        dialog.setHeaderText("Login as");
        dialog.showAndWait();

        final var clientName = dialog.getEditor().getText();
        getOutput().ifPresent(output -> output.onLogin(clientName));
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
    public void onQuit() {
        Platform.exit();
    }

    @Override
    public void onAbout() {
        // TODO
    }
}
