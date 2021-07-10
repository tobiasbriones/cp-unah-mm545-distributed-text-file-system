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

package io.github.tobiasbriones.cp.rmifilesystem.model.io;

import java.io.File;
import java.io.Serial;

/**
 * Creates a JavaFile type to avoid confusion between the domain File and the
 * Java physical java.io.File.
 *
 * @author Tobias Briones
 */
public final class JavaFile extends File {
    public static JavaFile from(CommonFile commonFile) {
        return from(commonFile.path());
    }

    public static JavaFile from(CommonPath commonPath) {
        return new JavaFile(commonPath.value());
    }

    @Serial
    private static final long serialVersionUID = -38976411105819394L;

    public JavaFile(String pathname) {
        super(pathname);
    }

    public JavaFile(String parent, String child) {
        super(parent, child);
    }

    public JavaFile(File parent, String child) {
        super(parent, child);
    }
}
