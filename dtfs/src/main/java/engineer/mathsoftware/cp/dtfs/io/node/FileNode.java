// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io.node;

import engineer.mathsoftware.cp.dtfs.io.CommonPath;
import engineer.mathsoftware.cp.dtfs.io.File;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Tobias Briones
 */
public final class FileNode implements Serializable,
                                       Node<File> {
    @Serial
    private static final long serialVersionUID = 927919524544928027L;
    private final File file;

    public FileNode(File file) {
        this.file = file;
    }

    @Override
    public File commonFile() {
        return file;
    }

    @Override
    public CommonPath commonPath() {
        return file.path();
    }

    @Override
    public String toString() {
        return file.path().value();
    }
}
