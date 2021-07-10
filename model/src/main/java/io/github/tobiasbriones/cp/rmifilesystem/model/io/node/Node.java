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

package io.github.tobiasbriones.cp.rmifilesystem.model.io.node;

import io.github.tobiasbriones.cp.rmifilesystem.model.io.CommonFile;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.JavaFile;

/**
 * @author Tobias Briones
 */
public sealed interface Node<T extends CommonFile> permits DirectoryNode, FileNode {
    T commonFile();

    default boolean isDirectory() {
        return commonFile() instanceof Directory;
    }

    default boolean isFile() {
        return commonFile() instanceof File;
    }

    default JavaFile toJavaFile() {
        return JavaFile.from(commonFile());
    }
}
