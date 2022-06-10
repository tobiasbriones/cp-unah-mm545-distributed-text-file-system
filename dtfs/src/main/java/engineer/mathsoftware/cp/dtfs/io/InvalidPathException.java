// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io;

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
