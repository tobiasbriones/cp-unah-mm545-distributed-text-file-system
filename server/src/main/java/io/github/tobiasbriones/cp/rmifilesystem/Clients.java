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

    public static void setValid(File file, String clientName) throws IOException {

        System.out.println("Setting valid: "+file.toString());
        System.out.println(clientName);
        final var root = new File(ROOT);
        final var client = new File(root, clientName + ".invalid.txt");

        if (!client.exists()) {
            return;
        }
        final var content = Files.readString(client.toPath());

        final var newContent = Arrays.stream(content.split(System.lineSeparator()))
                                     .filter(s -> !s.equals(file.toString()))
                                     .reduce((s1, s2) -> s1 + System.lineSeparator() + s2);

        if (newContent.isPresent()) {
            Files.writeString(client.toPath(), newContent.get());
        }
    }

    public static void setInvalid(File file) {
        if (!file.toString().endsWith(".txt")) {
            return;
        }
        final var root = new File(ROOT);
        final var list = root.listFiles();

        if (list != null) {
            Arrays.stream(list)
                  .filter(f -> f.toString().endsWith(".invalid.txt"))
                  .forEach(f -> addInvalidFile(file, f));
        }
    }

    private static void addInvalidFile(File file, File client) {
        try {
            final var currentContent = Files.readString(client.toPath());
            final var newContent = currentContent + System.lineSeparator() + file.toString();
            Files.writeString(client.toPath(), newContent);
        }
        catch (IOException e) {
            e.printStackTrace(); // :D
        }
    }

    private static String toClientInvalidFileName(String clientName) {
        return clientName + ".invalid.txt";
    }

    private Clients() {}
}
