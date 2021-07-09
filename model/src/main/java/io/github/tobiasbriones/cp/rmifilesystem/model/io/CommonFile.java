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

/**
 * @author Tobias Briones
 */
public sealed interface CommonFile permits Directory {
    /**
     * Standardize to use this character to define the system path separator
     * character at the domain model level (just for CommonFile objects) but not
     * for anything else. This is useful for this application to persist the
     * file path as string and forget whether the file separator is "/", "\",
     * ":" or ";".
     */
    char SEPARATOR_CHAR = '/';

    /**
     * See {@link CommonFile#SEPARATOR_CHAR}.
     */
    String SEPARATOR = String.valueOf(SEPARATOR_CHAR);

    String path();

    String name();
}
