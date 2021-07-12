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
import javafx.scene.text.Text;

import java.io.IOException;

final class FilesOutput implements Files.Output {
    private final FileSystemService service;
    private final Editor.Input editorInput;

    FilesOutput(FileSystemService service, Editor.Input editorInput) {
        this.service = service;
        this.editorInput = editorInput;
    }

    @Override
    public void onOpenFile(File.TextFile file) {
        final String content = loadFile(file);

        editorInput.setWorkingFile(file, content);
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
        final String content = service.readTextFile(file);
        AppLocalFiles.writeFile(file, content);
    }
}
