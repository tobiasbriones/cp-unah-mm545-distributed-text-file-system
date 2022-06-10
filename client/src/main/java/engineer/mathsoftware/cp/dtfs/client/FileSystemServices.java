// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client;

import engineer.mathsoftware.cp.dtfs.FileSystemService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @author Tobias Briones
 */
public final class FileSystemServices {
    public static final String HOST = "localhost"; // Registry hostname
    private static final int PORT = 1099;
    private static final String SERVICE_NAME = "RMIServer"; // Registry server name

    public static FileSystemService obtainService() throws RemoteException, NotBoundException {
        return (FileSystemService) LocateRegistry.getRegistry(HOST, PORT)
                                                 .lookup(SERVICE_NAME);
    }

    private FileSystemServices() {}
}
