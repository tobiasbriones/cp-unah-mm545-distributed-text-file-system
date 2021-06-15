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

package io.github.tobiasbriones.cp.rmifilesystem.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Tobias Briones
 */
public final class AppLocalFiles {
    private static final String REG_FILE_NAME = ".fsreg";
    private static final String RELATIVE_ROOT = "fs";
    private static final String ROOT = System.getProperty("user.dir") + File.separator + RELATIVE_ROOT;

    public static List<File> readFs() throws IOException {
        final var reg = readRegFile();
        return Arrays.stream(reg.split(System.lineSeparator()))
                     .map(File::new)
                     .toList();
    }

    public static void updateFs(Collection<? extends File> fs) throws IOException {
        final Optional<String> str = fs.stream()
                                       .map(File::toString)
                                       .reduce((s1, s2) -> s1 + System.lineSeparator() + s2);

        if (str.isPresent()) {
            final var path = Path.of(ROOT, REG_FILE_NAME);

            Files.writeString(path, str.get());
        }
    }

    public static String readFile(File file) throws IOException {
        final var absFile = new File(ROOT, file.toString());

        if (!absFile.exists()) {
            return "";
        }
        return Files.readString(absFile.toPath());
    }

    public static void storeFile(File file, CharSequence content) throws IOException {
        final var absFile = new File(ROOT, file.toString());

        if (!absFile.getName().endsWith(".txt")) {
            return;
        }
        if (!absFile.exists() || absFile.isDirectory()) {
            final var dir = absFile.getParentFile();

            if (!dir.exists() || !dir.isDirectory()) {
                if (!dir.mkdirs()) {
                    final var msg = "Fail to create file parent dirs: " + absFile;
                    throw new IOException(msg);
                }
            }
            if (!absFile.createNewFile()) {
                final var msg = "Fail to create file: " + absFile;
                throw new IOException(msg);
            }
        }
        Files.writeString(absFile.toPath(), content);
    }

    public static void storeDirectory(File dir) throws IOException {
        final var absDir = new File(ROOT, dir.toString());

        if (absDir.exists() && absDir.isDirectory()) {
            return;
        }
        if (!absDir.mkdirs()) {
            final var msg = "Fail to make directory: " + absDir;
            throw new IOException(msg);
        }
    }

    private static String readRegFile() throws IOException {
        final var file = new File(ROOT, REG_FILE_NAME);
        System.out.println(file);
        if (!file.exists()) {
            file.createNewFile();
        }
        return Files.readString(file.toPath());
    }

    private AppLocalFiles() {}
}
