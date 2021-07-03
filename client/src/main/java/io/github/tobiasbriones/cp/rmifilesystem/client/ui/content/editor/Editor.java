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

package io.github.tobiasbriones.cp.rmifilesystem.client.ui.content.editor;

import io.github.tobiasbriones.cp.rmifilesystem.model.ClientFile;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.MvpView;
import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import javafx.scene.Node;

import java.io.File;

/**
 * @author Tobias Briones
 */
public final class Editor implements Initializable {
    public interface Input {
        void setWorkingFile(ClientFile file, String content);

        void update();
    }

    interface Controller {
        void onSaveButtonClick();
    }

    interface View extends MvpView<Controller> {
        String getContent();

        void setContent(String value);
    }

    interface Presenter extends MvpPresenter<Void>, Controller, Input {
        void setService(FileSystemService value);
    }

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
        return presenter;
    }

    public void setService(FileSystemService value) {
        presenter.setService(value);
    }

    @Override
    public void init() {
        presenter.init();
    }
}
