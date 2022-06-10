// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io;

/**
 * @author Tobias Briones
 */
public record Directory(CommonPath path) implements CommonFile {
    public static Directory of() {
        return new Directory(CommonPath.of());
    }

    public static Directory of(CommonPath path) {
        return new Directory(path);
    }

    public Directory(String path) {
        this(new CommonPath(path));
    }

    public boolean isRoot() {
        return path.value().equals(CommonPath.ROOT_PATH);
    }
}
