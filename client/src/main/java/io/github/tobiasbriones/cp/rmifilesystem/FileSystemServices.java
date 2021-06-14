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

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public final class FileSystemServices {
    private static final String SERVICE_NAME = "FileSystemService";

    public static FileSystemService obtainService() throws RemoteException, NotBoundException {
        return (FileSystemService) LocateRegistry.getRegistry()
                                                 .lookup(SERVICE_NAME);
    }

    private FileSystemServices() {}
}
