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

import io.github.tobiasbriones.cp.rmifilesystem.client.ui.core.AbstractMvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.client.io.AppLocalFiles;
import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;

import java.io.File;
import java.io.IOException;

/**
 * @author Tobias Briones
 */
final class EditorPresenter extends AbstractMvpPresenter<Void> implements Editor.Presenter {
    private final Editor.View view;
    private FileSystemService service;
    private File currentFile;

    EditorPresenter(Editor.View view) {
        super();
        this.view = view;
        this.service = null;
        this.currentFile = null;
    }

    @Override
    public void init() {
        view.createView();
        view.bindEvents(this);
    }

    @Override
    public void setService(FileSystemService value) {
        service = value;
    }

    @Override
    public void onSaveButtonClick() {
        if (service == null || currentFile == null) {
            return;
        }
        final var content = view.getContent();
        saveContent(content);
    }

    @Override
    public void setWorkingFile(File file, String content) {
        currentFile = file;
        view.setContent(content);
    }

    @Override
    public void update() {
        if (currentFile == null) {
            return;
        }
        try {
            final var content = service.readTextFile(currentFile);

            AppLocalFiles.storeFile(currentFile, content);
            view.setContent(content);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveContent(String content) {
        try {
            service.writeTextFile(currentFile, content);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
