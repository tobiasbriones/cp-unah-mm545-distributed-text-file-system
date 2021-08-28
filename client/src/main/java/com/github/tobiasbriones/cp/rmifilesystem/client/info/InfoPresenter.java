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

package com.github.tobiasbriones.cp.rmifilesystem.client.info;

import com.github.tobiasbriones.cp.rmifilesystem.mvp.AbstractMvpPresenter;

/**
 * @author Tobias Briones
 */
final class InfoPresenter extends AbstractMvpPresenter<Void> implements Info.Presenter {
    private final Info.View view;

    InfoPresenter(Info.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.createView();
    }
}
