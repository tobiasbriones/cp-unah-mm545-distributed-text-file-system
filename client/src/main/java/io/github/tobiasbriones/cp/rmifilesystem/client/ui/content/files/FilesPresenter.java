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

import io.github.tobiasbriones.cp.rmifilesystem.client.io.AppLocalFiles;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.AbstractMvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;

import java.io.File;
import java.io.IOException;

/**
 * @author Tobias Briones
 */
final class FilesPresenter extends AbstractMvpPresenter<Files.Output> implements Files.Presenter {
    private final Files.View view;
    private FileSystemService service;

    FilesPresenter(Files.View view) {
        super();
        this.view = view;
        service = null;
    }

    @Override
    public void init() {
        view.setController(this);
        view.createView();
        update();
    }

    @Override
    public void onCreateButtonClick() {
        final var fileName = view.getCreateInputText();

        if (!fileName.isBlank()) {
            final var file = new File(fileName);

            if (fileName.endsWith(".txt")) {
                createNewFile(file);
            }
            else {
                createNewDir(file);
            }
        }
    }

    @Override
    public void onItemClick(File file) {
        if (!file.toString().endsWith(".txt")) {
            return;
        }
        getOutput().ifPresent(output -> output.onOpenFile(file));
    }

    @Override
    public void setService(FileSystemService value) {
        service = value;
    }

    @Override
    public void update() {
        try {
            final var fs = AppLocalFiles.readFs();
            view.clear();
            view.addItems(fs);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewFile(File file) {
        if (service == null) {
            return;
        }
        try {
            service.writeTextFile(file, "");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewDir(File file) {
        if (service == null) {
            return;
        }
        try {
            service.writeDir(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
