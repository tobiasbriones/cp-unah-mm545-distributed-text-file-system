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

import io.github.tobiasbriones.cp.rmifilesystem.client.AppLocalFiles;
import io.github.tobiasbriones.cp.rmifilesystem.client.content.editor.Editor;
import io.github.tobiasbriones.cp.rmifilesystem.client.content.files.Files;
import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.Nothing;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.Result;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileContent;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileRepository;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * @author Tobias Briones
 */
final class EditorOutput implements Editor.Output {
    private final Files.Input filesInput;
    private final Editor.Input editorInput;
    private final TextFileRepository repository;
    private FileSystemService service;

    EditorOutput(Files.Input filesInput, Editor.Input editorInput, TextFileRepository repository) {
        this.filesInput = filesInput;
        this.editorInput = editorInput;
        this.repository = repository;
        service = null;
    }

    @Override
    public void onFileAddedToChangelist(File file) {
        filesInput.update();
    }

    @Override
    public void onPush(File file) {
        if (service == null) {
            return;
        }
        if (file instanceof File.TextFile f) {
            pushFile(f);
        }
    }

    @Override
    public void onPull(File file) {
        if (service == null) {
            return;
        }
        if (file instanceof File.TextFile f) {
            pullFile(f);
        }
    }

    void setService(FileSystemService value) {
        service = value;
    }

    private void pushFile(File.TextFile f) {
        final Result<TextFileContent> result = repository.get(f);

        if (result instanceof Result.Success<TextFileContent> s) {
            pushFileContent(s.value());
            try {
                AppLocalFiles.removeFromChangeList(f);
                AppLocalFiles.setDownloaded(f);
                Content.updateLocalFs(service);
                filesInput.update();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (result instanceof Result.Failure<TextFileContent> fail) {
            fail.ifPresent(System.out::println);
        }
    }

    private void pushFileContent(TextFileContent content) {
        try {
            service.writeTextFile(content);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void pullFile(File.TextFile f) {
        try {
            final Result<TextFileContent> result = service.readTextFile(f);

            if (result instanceof Result.Success<TextFileContent> s) {
                updateLocalContent(s.value());

                try {
                    AppLocalFiles.removeFromChangeList(f);
                    AppLocalFiles.setDownloaded(f);
                    Content.updateLocalFs(service);

                    filesInput.update();
                    editorInput.update();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (result instanceof Result.Failure<TextFileContent> fail) {
                fail.ifPresent(System.out::println);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void updateLocalContent(TextFileContent content) {
        final var result = repository.set(content);

        if (result instanceof Result.Failure<Nothing> fail) {
            fail.ifPresent(System.out::println);
        }
    }
}
