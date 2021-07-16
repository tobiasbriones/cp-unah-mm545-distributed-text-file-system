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

package io.github.tobiasbriones.cp.rmifilesystem.client.content;

import io.github.tobiasbriones.cp.rmifilesystem.client.AppLocalFiles;
import io.github.tobiasbriones.cp.rmifilesystem.model.OnFileUpdateListener;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystems;
import javafx.application.Platform;

import java.io.IOException;
import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import static io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService.*;
import static io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem.*;

/**
 * Receives the file changes from the RMI server broadcast.
 *
 * @author Tobias Briones
 */
final class ContentOnFileUpdateListener extends UnicastRemoteObject implements OnFileUpdateListener {
    @Serial
    private static final long serialVersionUID = 7206688225773330503L;
    private final Content.OnLocalFsChangeListener l;

    ContentOnFileUpdateListener(Content.OnLocalFsChangeListener l) throws RemoteException {
        super();
        this.l = l;
    }

    @Override
    public void onFSChanged(RealTimeFileSystem system) throws RemoteException {
        updateLocalFs(system);
        post();
    }

    private void post() {
        Platform.runLater(l::update);
    }

    private static void updateLocalFs(RealTimeFileSystem system) {
        try {
            final Map<File, LastUpdateStatus> statuses = AppLocalFiles.readStatuses();
            final FileSystem fs = FileSystems.buildFileSystem(system, statuses);

            AppLocalFiles.saveFs(fs);
        }
        catch (IOException e) {
            // TODO Emit failure via GUI
            e.printStackTrace();
        }
    }
}
