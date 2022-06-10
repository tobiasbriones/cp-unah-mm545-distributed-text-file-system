// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Tobias Briones
 */
@FunctionalInterface
public interface OnFileUpdateListener extends Remote {
    // Pass the full system to make it easier to implement
    void onFSChanged(FileSystemService.RealTimeFileSystem system) throws RemoteException;
}
