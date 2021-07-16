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

package io.github.tobiasbriones.cp.rmifilesystem.client.content.editor;

import io.github.tobiasbriones.cp.rmifilesystem.client.AppLocalFiles;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.Result;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileContent;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileRepository;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.AbstractMvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;

import java.io.IOException;

/**
 * @author Tobias Briones
 */
final class EditorPresenter extends AbstractMvpPresenter<Void> implements Editor.Presenter {
    private final Editor.View view;
    private final TextFileRepository textFileRepository;
    private FileSystemService service;
    private File.TextFile currentFile;

    EditorPresenter(Editor.View view) {
        super();
        this.view = view;
        this.textFileRepository = AppLocalFiles.newTextFileRepository();
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
        final String content = view.getContent();

        saveContent(content);
    }

    @Override
    public void setWorkingFile(File.TextFile file, String content) {
        currentFile = file;

        setCurrentFile(content);
    }

    @Override
    public void closeFile(File.TextFile file) {
        if (file == currentFile) {
            currentFile = null;
            view.setWorkingFile("");
            view.setContent("");
        }
    }

    @Override
    public void update() {
        if (currentFile == null) {
            return;
        }
        try {
            final Result<TextFileContent> result = service.readTextFile(currentFile);

            if (result instanceof Result.Success<TextFileContent> s) {
                final TextFileContent content = s.value();

                textFileRepository.set(content);
                setCurrentFile(content.value());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentFile(String content) {
        view.setWorkingFile(currentFile.name());
        view.setContent(content);
    }

    private void saveContent(String content) {
        if (currentFile == null) {
            return;
        }
        try {
            service.writeTextFile(new TextFileContent(currentFile, content));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
