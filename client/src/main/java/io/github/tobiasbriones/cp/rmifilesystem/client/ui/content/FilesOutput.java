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

package io.github.tobiasbriones.cp.rmifilesystem.client.ui.content;

import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import io.github.tobiasbriones.cp.rmifilesystem.client.io.AppLocalFiles;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.content.editor.Editor;
import io.github.tobiasbriones.cp.rmifilesystem.client.ui.content.files.Files;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Map;

import static io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem.*;

final class FilesOutput implements Files.Output {
    private final Editor.Input editorInput;
    private FileSystemService service;

    FilesOutput(Editor.Input editorInput) {
        this.editorInput = editorInput;
        this.service = null;
    }

    @Override
    public void onOpenFile(File.TextFile file) {
        final String content = loadFile(file);

        editorInput.setWorkingFile(file, content);
    }

    void setService(FileSystemService value) {
        service = value;
    }

    private String loadFile(File.TextFile file) {
        try {
            updateFile(file);
            return AppLocalFiles.readFile(file).orElse("");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void updateFile(File.TextFile file) throws IOException {
        if (service == null) {
            return;
        }
        final String content = service.readTextFile(file);
        AppLocalFiles.writeFile(file, content);
        setDownloaded(file);
    }

    private static void setDownloaded(File file) throws IOException {
        final Map<File, LastUpdateStatus> statuses = AppLocalFiles.readStatuses();
        final LastUpdateStatus status = LastUpdateStatus.of(file);

        statuses.put(file, status);
        AppLocalFiles.saveStatuses(statuses);
    }
}
