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

package com.github.tobiasbriones.cp.rmifilesystem.model.io;

import java.io.Serializable;

/**
 * @author Tobias Briones
 */
public sealed interface CommonFile extends Serializable permits Directory, File {
    CommonPath path();

    default String name() {
        final String[] tokens = path().split();
        return tokens[tokens.length - 1];
    }
}
