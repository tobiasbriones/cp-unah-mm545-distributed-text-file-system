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

package engineer.mathsoftware.cp.dtfs.client.header;

import engineer.mathsoftware.cp.dtfs.mvp.AbstractMvpPresenter;

/**
 * @author Tobias Briones
 */
final class HeaderPresenter extends AbstractMvpPresenter<Void> implements Header.Presenter {
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
