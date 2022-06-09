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

package engineer.mathsoftware.cp.dtfs.io.node;

import engineer.mathsoftware.cp.dtfs.FileSystemService;
import engineer.mathsoftware.cp.dtfs.io.File;

import java.util.Map;

/**
 * @author Tobias Briones
 */
public final class FileSystems {
    public static FileSystem buildFileSystem(
        FileSystemService.RealTimeFileSystem system,
        Map<File, FileSystem.LastUpdateStatus> localStatuses
    ) {
        final var fs = new FileSystem(system.root());
        final Map<File, FileSystem.LastUpdateStatus> statuses = system.statuses();

        fs.updateStatuses(file -> getStatus(file, statuses, localStatuses));
        return fs;
    }

    private static FileSystem.Status getStatus(
        File file,
        Map<File, FileSystem.LastUpdateStatus> recentStatuses,
        Map<File, FileSystem.LastUpdateStatus> localStatuses
    ) {
        final FileSystem.LastUpdateStatus recent = recentStatuses.get(file);
        final FileSystem.LastUpdateStatus local = localStatuses.get(file);
        var isInvalid = true;

        if (recent != null && local != null) {
            isInvalid = recent.isInvalid(local);
        }
        else if (recent == null) { // The file has never been modified as it's not in the server changed status registry
            isInvalid = false;
        }
        return new FileSystem.Status(file, isInvalid);
    }

    private FileSystems() {}
}
