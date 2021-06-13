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

package io.github.tobiasbriones.cp.rmifilesystem.ui.content.files;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;

import java.io.File;

final class FilesPresenter extends AbstractMvpPresenter<Void> implements Files.Presenter {
    private final Files.View view;

    FilesPresenter(Files.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.setController(this);
        view.createView();

        view.addItem(new File("item1"));
        view.addItem(new File("item2"));
    }

    @Override
    public void onItemClick(File file) {
        System.out.println(file);
    }
}
