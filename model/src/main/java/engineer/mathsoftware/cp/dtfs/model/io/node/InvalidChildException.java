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

package engineer.mathsoftware.cp.dtfs.model.io.node;

import engineer.mathsoftware.cp.dtfs.model.io.CommonFile;
import engineer.mathsoftware.cp.dtfs.model.io.Directory;

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
