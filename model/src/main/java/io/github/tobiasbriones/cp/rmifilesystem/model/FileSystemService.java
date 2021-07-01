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

package io.github.tobiasbriones.cp.rmifilesystem.model;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Tobias Briones
 */
public interface FileSystemService extends Remote {
    List<ClientFile> getFileSystem() throws RemoteException;

    List<ClientFile> getInvalidFiles(String clientName) throws IOException;

    String readTextFile(ClientFile file) throws IOException;

    String readTextFile(ClientFile file, String clientName) throws IOException;

    void writeDir(ClientFile file) throws IOException;

    void writeTextFile(ClientFile file, String content) throws IOException;

    boolean addOnFileUpdateListener(OnFileUpdateListener l) throws RemoteException;
}
