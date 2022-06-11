// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io;

import java.io.Serializable;

/**
 * @author Tobias Briones
 */
public sealed interface CommonFile extends Serializable permits Directory,
                                                                File {
    default String name() {
        final String[] tokens = path().split();
        return tokens[tokens.length - 1];
    }

    CommonPath path();
}
