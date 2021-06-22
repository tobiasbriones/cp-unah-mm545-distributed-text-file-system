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

package io.github.tobiasbriones.cp.rmifilesystem.server;

import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Tobias Briones
 */
public final class Main {
    private static final int PORT = 1099;

    public static void main(String[] args) {
        final var main = new Main();

        main.start();
    }

    private FileSystemService server;

    private Main() {
        server = null;

        try {
            server = new AppFileSystemService();
        }
        catch (RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void start() {
        try {
            final Registry registry = LocateRegistry.createRegistry(PORT);
            registry.rebind("FileSystemService", server);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
