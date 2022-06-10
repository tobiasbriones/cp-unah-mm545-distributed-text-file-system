// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io.node;

import engineer.mathsoftware.cp.dtfs.io.CommonFile;
import engineer.mathsoftware.cp.dtfs.io.Directory;

import java.io.Serial;

/**
 * @author Tobias Briones
 */
public final class InvalidChildException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2032717105378391322L;

    private static String getMessage(Directory parent, CommonFile invalidChild) {
        return """
               Invalid child %s for parent %s
               """.formatted(invalidChild, parent);
    }

    InvalidChildException(Directory parent, CommonFile invalidChild) {
        super(getMessage(parent, invalidChild));
    }
}
