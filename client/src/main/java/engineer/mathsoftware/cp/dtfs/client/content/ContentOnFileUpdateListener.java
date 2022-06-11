// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.content;

import engineer.mathsoftware.cp.dtfs.FileSystemService;
import engineer.mathsoftware.cp.dtfs.OnFileUpdateListener;
import engineer.mathsoftware.cp.dtfs.client.AppLocalFiles;
import engineer.mathsoftware.cp.dtfs.io.File;
import engineer.mathsoftware.cp.dtfs.io.node.FileSystem;
import engineer.mathsoftware.cp.dtfs.io.node.FileSystems;
import javafx.application.Platform;

import java.io.IOException;
import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import static engineer.mathsoftware.cp.dtfs.FileSystemService.RealTimeFileSystem;

/**
 * Receives the file changes from the RMI server broadcast.
 *
 * @author Tobias Briones
 */
class ContentOnFileUpdateListener extends UnicastRemoteObject implements OnFileUpdateListener {
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
            var statuses = AppLocalFiles.readStatuses();
            var fs = FileSystems.buildFileSystem(system, statuses);
            AppLocalFiles.saveFs(fs);
        }
        catch (IOException e) {
            // TODO Emit failure via GUI
            e.printStackTrace();
        }
    }
}
