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

package engineer.mathsoftware.cp.dtfs.impl.io.file.text;

import engineer.mathsoftware.cp.dtfs.model.io.CommonPath;

import java.io.File;
import java.nio.file.Path;

/**
 * @author Tobias Briones
 */
public final class CommonPaths {
    public static Path toPath(Path root, CommonPath commonPath) {
        final var sb = new StringBuilder(5);

        for (final String token : commonPath.split()) {
            sb.append(token);
            sb.append(File.separator);
        }
        return Path.of(root.toString(), sb.toString());
    }

    private CommonPaths() {}
}
