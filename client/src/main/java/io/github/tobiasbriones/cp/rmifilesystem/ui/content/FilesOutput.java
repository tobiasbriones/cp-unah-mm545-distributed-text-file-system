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

package io.github.tobiasbriones.cp.rmifilesystem.ui.content;

import io.github.tobiasbriones.cp.rmifilesystem.FileSystemService;
import io.github.tobiasbriones.cp.rmifilesystem.ui.content.editor.Editor;
import io.github.tobiasbriones.cp.rmifilesystem.ui.content.files.Files;

import java.io.File;
import java.io.IOException;

final class FilesOutput implements Files.Output {
    private final FileSystemService service;
    private final Editor.Input editorInput;

    FilesOutput(FileSystemService service, Editor.Input editorInput) {
        this.service = service;
        this.editorInput = editorInput;
    }

    @Override
    public void onOpenFile(File file) {
        final var content = loadFile(file);
        editorInput.setContent(content);
    }

    private String loadFile(File file) {
        try {
            return service.readTextFile(file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
