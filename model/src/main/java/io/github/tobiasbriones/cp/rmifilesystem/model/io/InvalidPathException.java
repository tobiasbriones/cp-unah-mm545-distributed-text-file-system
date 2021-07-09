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

import java.io.Serial;

/**
 * @author Tobias Briones
 */
public final class InvalidPathException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6048722360769215689L;

    InvalidPathException(String invalidPathValue) {
        super(getMessage(invalidPathValue));
    }

    private static String getMessage(String invalidPath) {
        return """
               Invalid CommonFile path separator in given path: %s
               """.formatted(invalidPath);
    }
}
