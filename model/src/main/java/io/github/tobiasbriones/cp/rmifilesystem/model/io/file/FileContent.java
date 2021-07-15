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

import io.github.tobiasbriones.cp.rmifilesystem.model.io.CommonFile;

/**
 * Defines the {@link CommonFile}'s content. The content might be a String for a
 * text file for example.
 *
 * @author Tobias Briones
 */
public interface FileContent<T extends CommonFile, C> {
    /**
     * Returns the file for which this content belongs to.
     *
     * @return the file for which this content belongs to
     */
    T file();

    /**
     * Returns the content of its associated file.
     *
     * @return the content of its associated file
     */
    C value();
}
