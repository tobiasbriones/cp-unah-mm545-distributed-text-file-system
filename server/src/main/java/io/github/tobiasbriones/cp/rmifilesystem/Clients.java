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
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tobias Briones
 */
public final class Clients {
    private static final String ROOT_CLIENTS_DIR = "clients";
    private static final String ROOT = System.getProperty("user.dir") + File.separator + ROOT_CLIENTS_DIR;

    public static List<File> loadInvalidFiles(String clientName) throws IOException {
        final var root = new File(ROOT);
        final var clientFile = new File(root, toClientInvalidFileName(clientName));

        System.out.println(clientFile);
        if (!clientFile.exists()) {
            if (!clientFile.createNewFile()) {
                final var msg = "Fail to create client file";
                throw new IOException(msg);
            }
        }
        System.out.println(Files.readString(clientFile.toPath()));
        return Arrays.stream(
            Files.readString(clientFile.toPath())
                 .split(System.lineSeparator())
        ).map(File::new).toList();
    }

    private static String toClientInvalidFileName(String clientName) {
        return clientName + ".invalid.txt";
    }

    private Clients() {}
}
