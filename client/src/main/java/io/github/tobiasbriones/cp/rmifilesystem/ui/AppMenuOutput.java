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

package io.github.tobiasbriones.cp.rmifilesystem.ui;

import io.github.tobiasbriones.cp.rmifilesystem.ui.header.Header;
import io.github.tobiasbriones.cp.rmifilesystem.ui.menu.AppMenu;

import java.io.File;

/**
 * @author Tobias Briones
 */
final class AppMenuOutput implements AppMenu.Output {
    private final Header.Input headerInput;

    AppMenuOutput(Header.Input headerInput) {
        this.headerInput = headerInput;
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
        headerInput.setUser(clientName);
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
