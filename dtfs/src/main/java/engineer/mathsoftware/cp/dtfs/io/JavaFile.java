// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io;

import java.io.File;
import java.io.Serial;

/**
 * Creates a JavaFile type to avoid confusion between the domain File and the
 * Java physical java.io.File.
 *
 * @author Tobias Briones
 */
public final class JavaFile extends File {
    @Serial
    private static final long serialVersionUID = -38976411105819394L;

    public static JavaFile from(CommonFile commonFile) {
        return from(commonFile.path());
    }

    public static JavaFile from(CommonPath commonPath) {
        return new JavaFile(commonPath.value());
    }

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
