// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io.file;

import java.io.Serializable;

/**
 * @author Tobias Briones
 */
public record Nothing() implements Serializable {
    public static final Nothing Nothing = new Nothing();

    public static Nothing of() {
        return Nothing;
    }

    @Override
    public String toString() {
        return "Nothing";
    }
}
