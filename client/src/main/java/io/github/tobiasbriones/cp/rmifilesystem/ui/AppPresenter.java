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

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.header.Header;

/**
 * @author Tobias Briones
 */
final class AppPresenter extends AbstractMvpPresenter<Void> implements App.Presenter {
    private final App.View view;
    private final Header.Input headerInput;

    AppPresenter(App.View view, Header.Input headerInput) {
        super();
        this.view = view;
        this.headerInput = headerInput;
    }

    @Override
    public void init() {
        view.createView();
        headerInput.setUser("Unknown user");
    }
}
