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

package io.github.tobiasbriones.cp.rmifilesystem.ui.content.files;

import io.github.tobiasbriones.cp.rmifilesystem.FileSystemService;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;

import java.io.File;
import java.rmi.RemoteException;

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
        loadFs();
    }

    @Override
    public void onItemClick(File file) {
        getOutput().ifPresent(output -> output.onOpenFile(file));
    }

    @Override
    public void setService(FileSystemService value) {
        service = value;
    }

    private void loadFs() {
        if (service == null) {
            return;
        }
        try {
            final var fs = service.getFileSystem();
            view.addItems(fs);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
