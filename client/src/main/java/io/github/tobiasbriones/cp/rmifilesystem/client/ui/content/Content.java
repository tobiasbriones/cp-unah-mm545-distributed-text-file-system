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

package io.github.tobiasbriones.cp.rmifilesystem.client.ui.content;

import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.content.editor.Editor;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.content.files.Files;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.core.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.core.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.core.MvpView;
import io.github.tobiasbriones.cp.rmifilesystem.client.io.AppLocalFiles;
import javafx.scene.Node;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * @author Tobias Briones
 */
public final class Content implements Initializable {
    interface View extends MvpView<Void> {}

    interface Presenter extends MvpPresenter<Void> {}

    public static Content newInstance() {
        final var config = new ChildrenConfig(
            Files.newInstance(),
            Editor.newInstance()
        );
        return new Content(config);
    }

    record ChildrenConfig(
        Files files,
        Editor editor
    ) {
        ViewConfig newViewConfig() {
            return new ViewConfig(
                files.getView(),
                editor.getView()
            );
        }
    }

    record ViewConfig(
        Node filesView,
        Node editorView
    ) {}

    private final View view;
    private final Presenter presenter;
    private final Files files;
    private final Editor editor;
    private FileSystemService service;

    private Content(ChildrenConfig config) {
        view = new ContentView(config.newViewConfig());
        presenter = new ContentPresenter(view);
        files = config.files();
        editor = config.editor();
        service = null;
    }

    public Node getView() {
        return view.getView();
    }

    public void setService(FileSystemService value) {
        service = value;
        files.setService(value);
        editor.setService(value);
        bindServiceListener();
        updateFs(service);
    }

    @Override
    public void init() {
        setOutputs();
        presenter.init();
        files.init();
        editor.init();
    }

    private void setOutputs() {
        files.setOutput(new FilesOutput(service, editor.getInput()));
    }

    private void bindServiceListener() {
        try {
            service.addOnFileUpdateListener(
                new ContentOnFileUpdateListener(service, files, editor)
            );
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    static void updateFs(FileSystemService service) {
        try {
            final var fs = service.getFileSystem();

            AppLocalFiles.updateFs(fs);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
