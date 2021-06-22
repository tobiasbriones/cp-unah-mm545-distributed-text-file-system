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
import io.github.tobiasbriones.cp.rmifilesystem.model.OnFileUpdateListener;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.content.editor.Editor;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.content.files.Files;
import javafx.application.Platform;

import java.io.File;
import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Receives the file changes from the RMI server broadcast.
 *
 * @author Tobias Briones
 */
final class ContentOnFileUpdateListener extends UnicastRemoteObject implements OnFileUpdateListener {
    @Serial
    private static final long serialVersionUID = 7206688225773330503L;
    private final FileSystemService service;
    private final Files files;
    private final Editor editor;

    ContentOnFileUpdateListener(
        FileSystemService service,
        Files files,
        Editor editor
    ) throws RemoteException {
        super();
        this.service = service;
        this.files = files;
        this.editor = editor;
    }

    @Override
    public void onFileChanged(File file) throws RemoteException {
        Platform.runLater(() -> {
            Content.updateFs(service);
            files.getInput().update();
            editor.getInput().update();
        });
    }
}
