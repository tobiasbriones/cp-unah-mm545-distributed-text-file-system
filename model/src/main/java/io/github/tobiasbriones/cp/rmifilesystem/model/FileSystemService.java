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

import io.github.tobiasbriones.cp.rmifilesystem.model.io.CommonFile;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.Nothing;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.Result;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileContent;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.DirectoryNode;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import static io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem.*;

/**
 * @author Tobias Briones
 */
public interface FileSystemService extends Remote {
    record RealTimeFileSystem(
        DirectoryNode root,
        Map<File, LastUpdateStatus> statuses
    ) implements Serializable {}

    RealTimeFileSystem getRealTimeFileSystem() throws IOException;

    Result<TextFileContent> readTextFile(File.TextFile file) throws RemoteException;

    Result<Nothing> writeDirectory(Directory directory) throws RemoteException;

    Result<Nothing> writeTextFile(TextFileContent content) throws RemoteException;

    Result<Nothing> deleteFile(CommonFile file) throws RemoteException;

    boolean addOnFileUpdateListener(OnFileUpdateListener l) throws RemoteException;

    boolean removeOnFileUpdateListener(OnFileUpdateListener l) throws RemoteException;

    int recordObject(String name, Remote obj) throws RemoteException;
}
