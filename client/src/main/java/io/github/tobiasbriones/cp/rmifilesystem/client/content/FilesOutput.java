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
import io.github.tobiasbriones.cp.rmifilesystem.model.io.CommonFile;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.Result;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileContent;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileRepository;

import java.io.IOException;
import java.rmi.RemoteException;

final class FilesOutput implements Files.Output {
    private final Files.Input filesInput;
    private final Editor.Input editorInput;
    private final TextFileRepository repository;
    private FileSystemService service;

    FilesOutput(Files.Input filesInput, Editor.Input editorInput, TextFileRepository repository) {
        this.filesInput = filesInput;
        this.editorInput = editorInput;
        this.repository = repository;
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

    @Override
    public void onFileCreated(CommonFile file) {
        if (service == null) {
            return;
        }
        try {
            if (file instanceof Directory d) {
                service.writeDirectory(d);
            }
            else if (file instanceof File.TextFile f) {
                final var content = new TextFileContent(f, "");

                service.writeTextFile(content);
                onFileObtained(content);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileDeleted(CommonFile file) {
        if (service == null) {
            return;
        }
        try {
            service.deleteFile(file);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void setService(FileSystemService value) {
        service = value;
    }

    private String loadFile(File.TextFile file) {
        final Result<TextFileContent> result = repository.get(file);

        // Waiting for switch pattern matching! for proper monadic result design!
        if (result instanceof Result.Success<TextFileContent> s) {
            return s.value().value();
        }
        return "";
    }

    private void onFileObtained(TextFileContent content) throws IOException {
        final File.TextFile file = content.file();

        if (repository.exists(file)) {
            repository.set(content);
        }
        else {
            repository.add(content);
        }
        AppLocalFiles.setDownloaded(file);
        Content.updateLocalFs(service);
        filesInput.update();
        editorInput.update();
    }
}
