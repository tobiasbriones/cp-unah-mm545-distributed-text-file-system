// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.content;

import engineer.mathsoftware.cp.dtfs.mvp.AbstractMvpPresenter;

/**
 * @author Tobias Briones
 */
class ContentPresenter extends AbstractMvpPresenter<Void> implements Content.Presenter {
    private final Content.View view;

    ContentPresenter(Content.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.createView();
    }
}
