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

import java.io.File;

/**
 * @author Tobias Briones
 */
public class LocalClientFile extends ClientFile {
    private File rootFile;
    private File localFile;

    public LocalClientFile(File file) {
        super(file);
        rootFile = new File("");
        localFile = new File("");
    }

    public File getRootFile() {
        return rootFile;
    }

    public void setRootFile(File value) {
        rootFile = value;
        localFile = new File(rootFile, getFile().toString());
    }

    public File getLocalFile() {
        return localFile;
    }

    @Override
    public boolean isFile() {
        return localFile.isFile();
    }

    @Override
    public boolean isDirectory() {
        return localFile.isDirectory();
    }

    public boolean exists() {
        return localFile.exists();
    }
}
