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
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.Nothing;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.Result;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileContent;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileRepository;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.AbstractMvpPresenter;

import java.io.IOException;

/**
 * @author Tobias Briones
 */
final class EditorPresenter extends AbstractMvpPresenter<Editor.Output> implements Editor.Presenter {
    private final Editor.View view;
    private final TextFileRepository repository;
    private File.TextFile currentFile;

    EditorPresenter(Editor.View view, TextFileRepository repository) {
        super();
        this.view = view;
        this.repository = repository;
        this.currentFile = null;
    }

    @Override
    public void init() {
        view.createView();
        view.bindEvents(this);
    }

    @Override
    public void onPushButtonClick() {
        if (currentFile == null) {
            return;
        }
        onSaveButtonClick();
        getOutput().ifPresent(output -> output.onPush(currentFile));
    }

    @Override
    public void onPullButtonClick() {
        if (currentFile == null) {
            return;
        }
        getOutput().ifPresent(output -> output.onPull(currentFile));
    }

    @Override
    public void onSaveButtonClick() {
        final String content = view.getContent();

        saveContent(content);
    }

    @Override
    public void setWorkingFile(File.TextFile file, String content) {
        currentFile = file;

        setCurrentFileContent(content);
    }

    @Override
    public void closeFile(File.TextFile file) {
        if (file.equals(currentFile)) {
            clear();
        }
    }

    @Override
    public void update() {
        if (currentFile == null) {
            clear();
            return;
        }
        final Result<TextFileContent> result = repository.get(currentFile);

        if (result instanceof Result.Success<TextFileContent> s) {
            final TextFileContent content = s.value();

            setCurrentFileContent(content.value());
        }
        else if (result instanceof Result.Failure<TextFileContent> f) {
            f.ifPresent(System.out::println);
        }
    }

    private void setCurrentFileContent(String content) {
        view.setWorkingFile(currentFile.name());
        view.setContent(content);
    }

    private void clear() {
        currentFile = null;

        view.setWorkingFile("");
        view.setContent("");
    }

    private void saveContent(String content) {
        if (currentFile == null) {
            return;
        }
        final Result<Nothing> result = repository.set(new TextFileContent(currentFile, content));

        if (result instanceof Result.Success<Nothing>) {
            addToChangeList();
        }
        else if(result instanceof Result.Failure<Nothing> f) {
            f.ifPresent(System.out::println);
        }
    }

    private void addToChangeList() {
        try {
            AppLocalFiles.addToChangeList(currentFile);
            getOutput().ifPresent(output -> output.onFileAddedToChangelist(currentFile));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
