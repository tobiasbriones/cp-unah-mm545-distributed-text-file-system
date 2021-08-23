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

package com.github.tobiasbriones.cp.rmifilesystem.model;

import com.github.tobiasbriones.cp.rmifilesystem.model.io.CommonFile;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileContent;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.node.DirectoryNode;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.file.Nothing;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.file.Result;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * @author Tobias Briones
 */
public interface FileSystemService extends Remote {
    record RealTimeFileSystem(
        DirectoryNode root,
        Map<File, FileSystem.LastUpdateStatus> statuses
    ) implements Serializable {}

    RealTimeFileSystem getRealTimeFileSystem() throws IOException;

    Result<TextFileContent> readTextFile(File.TextFile file) throws RemoteException;

    Result<Nothing> writeDirectory(Directory directory) throws RemoteException;

    Result<Nothing> writeTextFile(TextFileContent content) throws RemoteException;

    Result<Nothing> deleteFile(CommonFile file) throws RemoteException;

    boolean addOnFileUpdateListener(OnFileUpdateListener l) throws RemoteException;

    boolean removeOnFileUpdateListener(OnFileUpdateListener l) throws RemoteException;
}
