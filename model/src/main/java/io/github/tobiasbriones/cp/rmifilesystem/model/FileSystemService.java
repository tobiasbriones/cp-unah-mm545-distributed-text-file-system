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

import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Tobias Briones
 */
public interface FileSystemService extends Remote {
    FileSystem getFileSystem() throws IOException;

    String readTextFile(File.TextFile file) throws IOException;

    void writeDir(Directory directory) throws IOException;

    void writeTextFile(File.TextFile file, String content) throws IOException;

    boolean addOnFileUpdateListener(OnFileUpdateListener l) throws RemoteException;
}
