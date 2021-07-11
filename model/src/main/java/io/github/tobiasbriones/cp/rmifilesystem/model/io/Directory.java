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

import java.io.Serializable;

/**
 * @author Tobias Briones
 */
public record Directory(CommonPath path) implements Serializable, CommonFile {
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
