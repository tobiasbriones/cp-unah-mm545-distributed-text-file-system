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

package io.github.tobiasbriones.cp.rmifilesystem.model;

import java.io.File;
import java.io.Serial;

/**
 * @author Tobias Briones
 */
public class RemoteClientFile extends ClientFile {
    @Serial
    private static final long serialVersionUID = -1537763654528505233L;

    public static RemoteClientFile fromClientFile(ClientFile clientFile) {
        final var file = new File(clientFile.getRelativePath());
        return new RemoteClientFile(file);
    }

    // TODO Add remote context
    public RemoteClientFile(File file) {
        super(file);
    }

    @Override
    public final boolean isFile() {
        // TODO
        return false;
    }

    @Override
    public final boolean isDirectory() {
        // TODO
        return false;
    }

    public final LocalClientFile toLocalClientFile(String rootPath) {
        return LocalClientFile.fromClientFile(this, rootPath);
    }
}
