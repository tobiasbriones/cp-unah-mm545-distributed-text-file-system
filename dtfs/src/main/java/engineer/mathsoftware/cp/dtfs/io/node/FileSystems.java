// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.io.node;

import engineer.mathsoftware.cp.dtfs.FileSystemService;
import engineer.mathsoftware.cp.dtfs.io.File;

import java.util.Map;

import static engineer.mathsoftware.cp.dtfs.io.node.FileSystem.*;

/**
 * @author Tobias Briones
 */
public final class FileSystems {
    public static FileSystem buildFileSystem(
        FileSystemService.RealTimeFileSystem system,
        Map<File, LastUpdateStatus> localStatuses
    ) {
        var fs = new FileSystem(system.root());
        var statuses = system.statuses();
        fs.updateStatuses(file -> getStatus(file, statuses, localStatuses));
        return fs;
    }

    private FileSystems() {}

    private static Status getStatus(
        File file,
        Map<File, LastUpdateStatus> recentStatuses,
        Map<File, LastUpdateStatus> localStatuses
    ) {
        var recent = recentStatuses.get(file);
        var local = localStatuses.get(file);
        var isInvalid = true;

        if (recent != null && local != null) {
            isInvalid = recent.isInvalid(local);
        }
        else if (recent == null) { // The file has never been modified as
            // it's not in the server changed status registry
            isInvalid = false;
        }
        return new Status(file, isInvalid);
    }
}
