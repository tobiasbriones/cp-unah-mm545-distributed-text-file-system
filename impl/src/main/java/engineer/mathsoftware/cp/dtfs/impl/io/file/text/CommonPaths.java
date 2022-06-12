// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.impl.io.file.text;

import engineer.mathsoftware.cp.dtfs.io.CommonPath;

import java.io.File;
import java.nio.file.Path;

/**
 * @author Tobias Briones
 */
public final class CommonPaths {
    public static Path toPath(Path root, CommonPath commonPath) {
        var sb = new StringBuilder(5);
        for (var token : commonPath.split()) {
            sb.append(token);
            sb.append(File.separator);
        }
        return Path.of(root.toString(), sb.toString());
    }

    private CommonPaths() {}
}
