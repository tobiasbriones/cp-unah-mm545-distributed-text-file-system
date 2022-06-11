// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.header;

import engineer.mathsoftware.cp.dtfs.mvp.AbstractMvpPresenter;

/**
 * @author Tobias Briones
 */
class HeaderPresenter extends AbstractMvpPresenter<Void> implements Header.Presenter {
    private final Header.View view;

    HeaderPresenter(Header.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.createView();
    }
}
