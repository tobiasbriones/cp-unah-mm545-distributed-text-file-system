// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io.file;

import engineer.mathsoftware.cp.dtfs.io.CommonFile;

/**
 * Defines a FileRepository to access and perform operations on any
 * {@link CommonFile}'s content.
 *
 * @param <F> type of the {@link CommonFile} used by this repository
 * @param <C> type of the {@link CommonFile}'s content
 *
 * @author Tobias Briones
 * @see FileContent
 */
public interface FileRepository<F extends CommonFile, C extends FileContent<F
    , ?>> {
    Result<C> get(F file);

    Result<Nothing> set(C content);

    Result<Nothing> add(C content);

    Result<Nothing> remove(F file);

    boolean exists(F file);
}
