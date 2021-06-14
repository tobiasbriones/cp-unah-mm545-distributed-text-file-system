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

package io.github.tobiasbriones.cp.rmifilesystem.ui.content.editor;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpView;
import javafx.scene.Node;

/**
 * @author Tobias Briones
 */
public final class Editor implements Initializable {
    public interface Input {
        void setContent(String value);
    }

    interface Controller {
        void onSave();
    }

    interface View extends MvpView<Controller>, Input {}

    interface Presenter extends MvpPresenter<Void>, Controller {}

    public static Editor newInstance() {
        return new Editor();
    }

    private final View view;
    private final Presenter presenter;

    private Editor() {
        view = new EditorView();
        presenter = new EditorPresenter(view);
    }

    public Node getView() {
        return view.getView();
    }

    public Input getInput() {
        return view;
    }

    @Override
    public void init() {
        presenter.init();
    }
}
