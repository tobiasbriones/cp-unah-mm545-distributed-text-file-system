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

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpView;
import javafx.scene.Node;

import java.io.File;
import java.util.List;

/**
 * @author Tobias Briones
 */
public final class Files implements Initializable {
    interface Controller {
        void onItemClick(File file);
    }

    interface View extends MvpView<Controller> {
        void setController(Controller value);

        void addItem(File file);

        void clear();

        default void addItems(List<? extends File> files) {
            files.forEach(this::addItem);
        }
    }

    interface Presenter extends MvpPresenter<Void>, Controller {}

    public static Files newInstance() {
        return new Files();
    }

    private final View view;
    private final Presenter presenter;

    private Files() {
        view = new FilesView();
        presenter = new FilesPresenter(view);
    }

    public Node getView() {
        return view.getView();
    }

    @Override
    public void init() {
        presenter.init();
    }
}
