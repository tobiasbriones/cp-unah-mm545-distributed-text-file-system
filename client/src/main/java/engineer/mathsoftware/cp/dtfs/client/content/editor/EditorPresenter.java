// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.content.editor;

import engineer.mathsoftware.cp.dtfs.client.AppLocalFiles;
import engineer.mathsoftware.cp.dtfs.io.File;
import engineer.mathsoftware.cp.dtfs.io.file.Nothing;
import engineer.mathsoftware.cp.dtfs.io.file.Result.Failure;
import engineer.mathsoftware.cp.dtfs.io.file.Result.Success;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileContent;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileRepository;
import engineer.mathsoftware.cp.dtfs.mvp.AbstractMvpPresenter;

import java.io.IOException;

/**
 * @author Tobias Briones
 */
class EditorPresenter extends AbstractMvpPresenter<Editor.Output> implements Editor.Presenter {
    private final Editor.View view;
    private final TextFileRepository repository;
    private File.TextFile currentFile;

    EditorPresenter(Editor.View view, TextFileRepository repository) {
        super();
        this.view = view;
        this.repository = repository;
        this.currentFile = null;
    }

    private void setCurrentFileContent(String content) {
        view.setWorkingFile(currentFile.name());
        view.setContent(content);
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
        var content = view.getContent();
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
        switch (repository.get(currentFile)) {
            case Success<TextFileContent> s -> {
                var content = s.value();
                setCurrentFileContent(content.value());
            }
            case Failure f -> f.ifPresent(System.out::println);
        }
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
        var result = repository.set(new TextFileContent(
            currentFile,
            content
        ));

        switch (result) {
            case Success<Nothing> s -> addToChangeList();
            case Failure f -> f.ifPresent(System.out::println);
        }
    }

    private void addToChangeList() {
        try {
            AppLocalFiles.addToChangeList(currentFile);
            getOutput().ifPresent(output -> output.onFileAddedToChangelist(
                currentFile));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
