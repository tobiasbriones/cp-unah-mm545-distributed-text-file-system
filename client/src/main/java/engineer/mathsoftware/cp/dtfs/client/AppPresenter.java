// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client;

import engineer.mathsoftware.cp.dtfs.client.header.Header;
import engineer.mathsoftware.cp.dtfs.mvp.AbstractMvpPresenter;

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
