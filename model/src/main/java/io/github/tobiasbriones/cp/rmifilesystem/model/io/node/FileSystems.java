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

import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;

import java.util.Map;

import static io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem.*;

/**
 * @author Tobias Briones
 */
public final class FileSystems {
    public static FileSystem buildFileSystem(
        FileSystemService.RealTimeFileSystem system,
        Map<File, LastUpdateStatus> localStatuses
    ) {
        final var fs = new FileSystem(system.root());
        final Map<File, LastUpdateStatus> statuses = system.statuses();

        fs.updateStatuses(file -> getStatus(file, statuses, localStatuses));
        return fs;
    }

    private static Status getStatus(
        File file,
        Map<File, LastUpdateStatus> recentStatuses,
        Map<File, LastUpdateStatus> localStatuses
    ) {
        final LastUpdateStatus recent = recentStatuses.get(file);
        final LastUpdateStatus local = localStatuses.get(file);
        var isInvalid = true;

        if (recent != null && local != null) {
            isInvalid = recent.isInvalid(local);
        }
        else if (recent == null) { // The file has never been modified as it's not in the server changed status registry
            isInvalid = false;
        }
        return new Status(file, isInvalid);
    }

    private FileSystems() {}
}
