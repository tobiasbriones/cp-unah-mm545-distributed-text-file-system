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

package io.github.tobiasbriones.cp.rmifilesystem.client.ui.content.files;

import io.github.tobiasbriones.cp.rmifilesystem.model.io.CommonFile;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.DirectoryNode;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.MvpView;
import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.Node;


/**
 * @author Tobias Briones
 */
public final class Files implements Initializable {
    public interface Input {
        void update();
    }

    public interface Output {
        void onOpenFile(File.TextFile file);
    }

    interface Controller {
        void onCreateButtonClick();

        void onItemClick(File.TextFile file);

        void onNewFileAction(DirectoryNode node);

        void onNewDirectoryAction(DirectoryNode node);

        void onDeleteAction(Node<?> node);
    }

    interface View extends MvpView<Controller> {
        void setController(Controller value);

        String getCreateInputText();

        void setRoot(DirectoryNode root);

        void clear();
    }

    interface Presenter extends MvpPresenter<Output>, Controller, Input {
        void setService(FileSystemService value);
    }

    public static Files newInstance() {
        return new Files();
    }

    private final View view;
    private final Presenter presenter;

    private Files() {
        view = new FilesView();
        presenter = new FilesPresenter(view);
    }

    public javafx.scene.Node getView() {
        return view.getView();
    }

    public Input getInput() {
        return presenter;
    }

    public void setService(FileSystemService value) {
        presenter.setService(value);
    }

    public void setOutput(Output value) {
        presenter.setOutput(value);
    }

    @Override
    public void init() {
        presenter.init();
    }
}
