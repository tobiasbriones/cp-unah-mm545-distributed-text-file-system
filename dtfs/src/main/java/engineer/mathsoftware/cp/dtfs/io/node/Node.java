// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io.node;

import engineer.mathsoftware.cp.dtfs.io.*;

/**
 * @author Tobias Briones
 */
public sealed interface Node<T extends CommonFile> permits DirectoryNode,
                                                           FileNode {
    T commonFile();

    CommonPath commonPath();

    default boolean isDirectory() {
        return commonFile() instanceof Directory;
    }

    default boolean isFile() {
        return commonFile() instanceof File;
    }

    default String toRecursiveString() {
        return Nodes.getString(this, "");
    }

    default JavaFile toJavaFile() {
        return JavaFile.from(commonFile());
    }
}
