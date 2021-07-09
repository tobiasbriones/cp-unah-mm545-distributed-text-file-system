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

package io.github.tobiasbriones.cp.rmifilesystem.model.io.node;

import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;

import java.io.Serial;

/**
 * @author Tobias Briones
 */
public final class CircularParentException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5468051786177282618L;

    private static String getMessage(Node node, Directory badParent) {
        return """
               Circular parent %s for node %s
               """.formatted(node.commonFile().path().value(), badParent.path());
    }

    public CircularParentException(Node node, Directory badParent) {
        super(getMessage(node, badParent));
    }
}
