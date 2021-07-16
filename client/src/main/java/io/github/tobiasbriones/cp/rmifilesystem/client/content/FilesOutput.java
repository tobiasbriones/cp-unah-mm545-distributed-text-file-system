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

package io.github.tobiasbriones.cp.rmifilesystem.client.content;

import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import io.github.tobiasbriones.cp.rmifilesystem.client.AppLocalFiles;
import io.github.tobiasbriones.cp.rmifilesystem.client.content.editor.Editor;
import io.github.tobiasbriones.cp.rmifilesystem.client.content.files.Files;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.Result;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileContent;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileRepository;

import java.io.IOException;
import java.util.Map;

import static io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem.*;

final class FilesOutput implements Files.Output {
    private final Editor.Input editorInput;
    private final TextFileRepository textFileRepository;
    private FileSystemService service;

    FilesOutput(Editor.Input editorInput) {
        this.editorInput = editorInput;
        this.textFileRepository = AppLocalFiles.newTextFileRepository();
        this.service = null;
    }

    @Override
    public void onOpenFile(File.TextFile file) {
        final String content = loadFile(file);

        editorInput.setWorkingFile(file, content);
    }

    @Override
    public void onCloseFile(File.TextFile file) {
        editorInput.closeFile(file);
    }

    void setService(FileSystemService value) {
        service = value;
    }

    private String loadFile(File.TextFile file) {
        try {
            updateFile(file);
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        final var result = textFileRepository.get(file);

        // Waiting for switch pattern matching! for proper monadic result design!
        if (result instanceof Result.Success<TextFileContent> s) {
            return s.value().value();
        }
        return "";
    }

    private void updateFile(File.TextFile file) throws IOException {
        if (service == null) {
            return;
        }
        final Result<TextFileContent> result = service.readTextFile(file);

        if (result instanceof Result.Success<TextFileContent> s) {
            onFileObtained(s.value());
        }
        else {
            throw new IOException("Fail to update file from service");
        }
    }

    private void onFileObtained(TextFileContent content) throws IOException {
        final File.TextFile file = content.file();

        if (textFileRepository.exists(file)) {
            textFileRepository.set(content);
        }
        else {
            textFileRepository.add(content);
        }
        setDownloaded(file);
    }

    private static void setDownloaded(File file) throws IOException {
        final Map<File, LastUpdateStatus> statuses = AppLocalFiles.readStatuses();
        final LastUpdateStatus status = LastUpdateStatus.of(file);

        statuses.put(file, status);
        AppLocalFiles.saveStatuses(statuses);
    }
}
