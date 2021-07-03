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
import java.io.Serial;
import java.io.Serializable;

/**
 * @author Tobias Briones
 */
public abstract class ClientFile implements Serializable {
    @Serial
    private static final long serialVersionUID = 6982279004706214659L;
    private final File file;

    protected ClientFile(File file) {
        this.file = file;
    }

    public final String getRelativePath() {
        return file.toString();
    }

    public abstract boolean isFile();

    public abstract boolean isDirectory();

    @Override
    public String toString() {
        return file.toString();
    }
}
