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

import io.github.tobiasbriones.cp.rmifilesystem.client.ui.header.Header;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.menu.AppMenu;
import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;

import java.io.File;
import java.io.Serializable;

/**
 * @author Tobias Briones
 */
final class AppMenuOutput implements AppMenu.Output {
    @FunctionalInterface
    interface Quit {
        void apply();
    }

    private final Header.Input headerInput;
    private final Quit quit;
    private FileSystemService service;

    AppMenuOutput(Header.Input headerInput, Quit quit) {
        this.headerInput = headerInput;
        this.quit = quit;
        this.service = null;
    }

    void setService(FileSystemService value) {
        service = value;
    }

    @Override
    public void onCreateNewFile(File file) {
        // TODO
    }

    @Override
    public void onCreateNewDirectory(File dirFile) {
        // TODO
    }

    @Override
    public void onLogin(String clientName) {
        headerInput.setUser("Login unsupported yet!");
    }

    @Override
    public void onQuit() {
        quit.apply();
    }

    @Override
    public void onSave() {
        // TODO
    }

    @Override
    public void onClose() {
        // TODO
    }
}
