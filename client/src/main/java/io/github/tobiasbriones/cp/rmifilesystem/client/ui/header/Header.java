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

package io.github.tobiasbriones.cp.rmifilesystem.client.ui.header;

import io.github.tobiasbriones.cp.rmifilesystem.mvp.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.MvpView;
import javafx.scene.Node;

/**
 * @author Tobias Briones
 */
public final class Header implements Initializable {
    public interface Input {
        void setUser(String value);
    }

    interface View extends MvpView<Void>, Input {}

    interface Presenter extends MvpPresenter<Void> {}

    private final View view;
    private final Presenter presenter;

    public Header() {
        view = new HeaderView();
        presenter = new HeaderPresenter(view);
    }

    public Node getView() {
        return view.getView();
    }

    public Input getInput() {
        return view;
    }

    public void init() {
        presenter.init();
    }
}
