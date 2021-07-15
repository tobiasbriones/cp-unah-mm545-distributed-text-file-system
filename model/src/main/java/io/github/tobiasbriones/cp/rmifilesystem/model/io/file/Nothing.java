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

package io.github.tobiasbriones.cp.rmifilesystem.model.io.file;

/**
 * @author Tobias Briones
 */
public record Nothing() {
    public static final Nothing Nothing = new Nothing();

    public static Nothing of() {
        return Nothing;
    }

    @Override
    public String toString() {
        return "Nothing";
    }
}
