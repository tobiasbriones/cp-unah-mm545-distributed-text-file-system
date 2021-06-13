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

import io.github.tobiasbriones.cp.rmifilesystem.ui.menu.AppMenu;

import java.io.File;

final class AppMenuOutput implements AppMenu.Output {
    AppMenuOutput() {}

    @Override
    public void onCreateNewFile(File file) {
        // TODO
    }

    @Override
    public void onCreateNewDirectory(File dirFile) {
        // TODO
    }

    @Override
    public void onSave() {
        // TODO
    }

    @Override
    public void onClose() {
        // TODO
    }

    @Override
    public void onLogin() {
        // TODO
    }
}
