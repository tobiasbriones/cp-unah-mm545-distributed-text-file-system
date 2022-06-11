// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs;

import engineer.mathsoftware.cp.dtfs.io.CommonFile;
import engineer.mathsoftware.cp.dtfs.io.Directory;
import engineer.mathsoftware.cp.dtfs.io.File;
import engineer.mathsoftware.cp.dtfs.io.file.Nothing;
import engineer.mathsoftware.cp.dtfs.io.file.Result;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileContent;
import engineer.mathsoftware.cp.dtfs.io.node.DirectoryNode;
import engineer.mathsoftware.cp.dtfs.io.node.FileSystem;

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

    Result<TextFileContent> readTextFile(File.TextFile file) throws
                                                             RemoteException;

    Result<Nothing> writeDirectory(Directory directory) throws RemoteException;

    Result<Nothing> writeTextFile(TextFileContent content) throws
                                                           RemoteException;

    Result<Nothing> deleteFile(CommonFile file) throws RemoteException;

    boolean addOnFileUpdateListener(OnFileUpdateListener l) throws
                                                            RemoteException;

    boolean removeOnFileUpdateListener(OnFileUpdateListener l) throws
                                                               RemoteException;
}
