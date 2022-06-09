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

package engineer.mathsoftware.cp.dtfs.server;

import com.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import com.github.tobiasbriones.cp.rmifilesystem.model.RegistryService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Tobias Briones
 */
public final class Main {
    private static final String REG_SERVER_NAME = "FileSystemService";
    private static final String FS_SERVER_NAME = "RMIServer";

    private record ServerConfig(
        String hostname,
        String type,
        String registryHostname // This arg is only used for FS server types
    ) {
        static final int PORT = 1099;
        static final String LOCAL_HOST = "localhost";
        static final String SERVER_TYPE_REG = "reg";
        static final String SERVER_TYPE_FS = "fs";

        static ServerConfig from(String[] args) {
            return new ServerConfig(
                getHostname(args),
                getServerType(args),
                getRegistryHostname(args)
            );
        }

        static String getHostname(String[] args) {
            return args.length > 0 ? args[0] : LOCAL_HOST;
        }

        static String getServerType(String[] args) {
            return args.length > 1 ? args[1] : SERVER_TYPE_FS;
        }

        static String getRegistryHostname(String[] args) {
            return args.length > 2 ? args[2] : "";
        }
    }

    public static void main(String[] args) {
        final var config = ServerConfig.from(args);
        final var main = new Main(config);

        main.start();
    }

    private final ServerConfig config;
    private FileSystemService server;

    private Main(ServerConfig config) {
        this.config = config;
        server = null;

        System.setProperty("java.rmi.server.hostname", config.hostname());
        try {
            server = new AppFileSystemService();
        }
        catch (RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void start() {
        if (config.type().equals(ServerConfig.SERVER_TYPE_REG)) {
            startRegServer();
        }
        else {
            startFileSystemServer();
        }
    }

    private void startRegServer() {
        try {
            final Registry registry = LocateRegistry.createRegistry(ServerConfig.PORT);

            System.out.println("Binding registry server at " + config.hostname());
            registry.rebind(REG_SERVER_NAME, server);
        }
        catch (RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void startFileSystemServer() {
        try {
            final String registryHostname = config.registryHostname();
            final Registry registry = LocateRegistry.getRegistry(registryHostname, ServerConfig.PORT);
            final var service = (RegistryService) registry.lookup(REG_SERVER_NAME);

            System.out.println("Binding file server at " + config.hostname());
            service.regObject(FS_SERVER_NAME, server);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
