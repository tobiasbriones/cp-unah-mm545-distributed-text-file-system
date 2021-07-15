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
 * Defines a FileRepository to access and perform operations on any
 * {@link CommonFile}'s content.
 *
 * @param <F> type of the {@link CommonFile} used by this repository
 * @param <C> type of the {@link CommonFile}'s content
 *
 * @see FileContent
 *
 * @author Tobias Briones
 */
public interface FileRepository<F extends CommonFile, C extends FileContent<F, ?>> {
    Result<C> get(F file);

    Result<Nothing> set(C content);

    Result<Nothing> add(C content);

    Result<Nothing> remove(F file);
}
