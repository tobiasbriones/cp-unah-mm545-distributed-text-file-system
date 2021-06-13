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

package io.github.tobiasbriones.cp.rmifilesystem;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Tobias Briones
 */
public interface FileSystemService extends Remote {
    List<File> getFileSystem() throws RemoteException;

    String readTextFile(File file) throws RemoteException;

    void writeTextFile(File file, String content) throws RemoteException;

    void addOnFileUpdateListener(OnFileUpdateListener l) throws RemoteException;
}
