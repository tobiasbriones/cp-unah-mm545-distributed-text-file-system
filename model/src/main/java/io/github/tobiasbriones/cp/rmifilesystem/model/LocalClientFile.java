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

package io.github.tobiasbriones.cp.rmifilesystem.model;

import java.io.*;
import java.nio.file.Path;

/**
 * @author Tobias Briones
 */
public class LocalClientFile extends ClientFile {
    @Serial
    private static final long serialVersionUID = -7184265387670681L;

    public static LocalClientFile fromClientFile(ClientFile clientFile, String rootPath) {
        final var file = new File(clientFile.getRelativePath());
        return new LocalClientFile(file, rootPath);
    }

    private File rootFile;
    private File localFile;

    public LocalClientFile(File file) {
        this(file, "");
    }

    public LocalClientFile(File file, String rootPath) {
        this(file, new File(rootPath));
    }

    public LocalClientFile(File file, File rootFile) {
        super(file);
        setRootFile(rootFile);
    }

    public final void setRootFile(File value) {
        rootFile = value;
        localFile = new File(rootFile, getRelativePath());
    }

    @Override
    public final boolean isFile() {
        return localFile.isFile();
    }

    @Override
    public final boolean isDirectory() {
        return localFile.isDirectory();
    }

    @Override
    public String toString() {
        return localFile.toString();
    }

    public final boolean exists() {
        return localFile.exists();
    }

    public final Path toPath() {
        return localFile.toPath();
    }

    public final RemoteClientFile toRemoteClientFile() {
        return RemoteClientFile.fromClientFile(this);
    }

    public final InputStream newFileInputStream() throws FileNotFoundException {
        return new FileInputStream(localFile);
    }

    public final boolean mkdirs() {
        return localFile.mkdirs();
    }
}
