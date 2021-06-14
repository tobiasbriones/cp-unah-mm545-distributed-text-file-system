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

package io.github.tobiasbriones.cp.rmifilesystem.ui.content;

import io.github.tobiasbriones.cp.rmifilesystem.OnFileUpdateListener;
import io.github.tobiasbriones.cp.rmifilesystem.ui.content.editor.Editor;
import io.github.tobiasbriones.cp.rmifilesystem.ui.content.files.Files;

import java.io.File;
import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

final class ContentOnFileUpdateListener extends UnicastRemoteObject implements OnFileUpdateListener {
    @Serial
    private static final long serialVersionUID = 7206688225773330503L;
    private final Files files;
    private final Editor editor;

    ContentOnFileUpdateListener(Files files, Editor editor) throws RemoteException {
        super();
        this.files = files;
        this.editor = editor;
    }

    @Override
    public void onFileChanged(File file) throws RemoteException {
        System.out.println(file);
    }
}
