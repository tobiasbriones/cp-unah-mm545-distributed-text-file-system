// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io.file;

import engineer.mathsoftware.cp.dtfs.io.CommonFile;

import java.io.Serializable;

/**
 * Defines the {@link CommonFile}'s content. The content might be a String for a
 * text file for example.
 *
 * @author Tobias Briones
 */
public interface FileContent<T extends CommonFile, C> extends Serializable {
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
