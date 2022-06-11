// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.content;

import engineer.mathsoftware.cp.dtfs.FileSystemService;
import engineer.mathsoftware.cp.dtfs.client.AppLocalFiles;
import engineer.mathsoftware.cp.dtfs.client.content.editor.Editor;
import engineer.mathsoftware.cp.dtfs.client.content.files.Files;
import engineer.mathsoftware.cp.dtfs.client.info.Info;
import engineer.mathsoftware.cp.dtfs.io.CommonFile;
import engineer.mathsoftware.cp.dtfs.io.Directory;
import engineer.mathsoftware.cp.dtfs.io.File;
import engineer.mathsoftware.cp.dtfs.io.file.Nothing;
import engineer.mathsoftware.cp.dtfs.io.file.Result;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileContent;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileRepository;
import javafx.application.Platform;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.function.Consumer;

final class FilesOutput implements Files.Output {
    private final TextFileRepository repository;
    private final Files.Input filesInput;
    private final Editor.Input editorInput;
    private final Info.Input infoInput;
    private FileSystemService service;
    FilesOutput(DependencyConfig config) {
        this.repository = config.repository();
        this.filesInput = config.filesInput();
        this.editorInput = config.editorInput();
        this.infoInput = config.infoInput();
        this.service = null;
    }

    void setService(FileSystemService value) {
        service = value;
    }

    @Override
    public void onOpenFile(File.TextFile file) {
        final String content = loadFile(file);

        editorInput.setWorkingFile(file, content);
        infoInput.end("Open file: " + file.path().value());
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
        createRemoteFileAsync(file);
    }

    @Override
    public void onFileDeleted(CommonFile file) {
        if (service == null) {
            return;
        }
        deleteRemoteFileAsync(file);
    }

    private String loadFile(File.TextFile file) {
        final Result<TextFileContent> result = repository.get(file);

        // Waiting for switch pattern matching! for proper monadic result
        // design!
        if (result instanceof Result.Success<TextFileContent> s) {
            return s.value().value();
        }
        return "";
    }

    // ---------- CREATE
    private void createRemoteFileAsync(CommonFile file) {
        if (file instanceof Directory d) {
            createRemoteDirectoryAsync(d);
        }
        else if (file instanceof File.TextFile f) {
            createRemoteTextFileAsync(f);
        }
    }

    private void createRemoteTextFileAsync(File.TextFile file) {
        final var content = new TextFileContent(file, "");
        final Consumer<Result<Nothing>> resultConsumer = result -> {
            if (result instanceof Result.Success<Nothing>) {
                Platform.runLater(() -> onNewRemoteFileWrote(content));
            }
            else {
                Platform.runLater(() -> onNewRemoteFileWriteError(file));
            }
        };
        final Runnable runnable = () -> {
            try {
                final Result<Nothing> result = service.writeTextFile(content);
                resultConsumer.accept(result);
            }
            catch (RemoteException e) {
                e.printStackTrace();
                resultConsumer.accept(Result.Failure.of(e));
            }
        };
        final var thread = new Thread(runnable);

        thread.start();
    }

    private void createRemoteDirectoryAsync(Directory directory) {
        final Consumer<Result<Nothing>> resultConsumer = result -> {
            if (result instanceof Result.Success<Nothing>) {
                Platform.runLater(() -> onRemoteDirectoryWrote(directory));
            }
            else {
                Platform.runLater(() -> onNewRemoteFileWriteError(directory));
            }
        };
        final Runnable runnable = () -> {
            try {
                final Result<Nothing> result =
                    service.writeDirectory(directory);
                resultConsumer.accept(result);
            }
            catch (RemoteException e) {
                e.printStackTrace();
                resultConsumer.accept(Result.Failure.of(e));
            }
        };
        final var thread = new Thread(runnable);

        thread.start();
    }

    private void onRemoteDirectoryWrote(Directory directory) {
        infoInput.end("Create remote directory: " + directory.path().value());
    }

    private void onNewRemoteFileWrote(TextFileContent content) {
        try {
            onFileObtained(content);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onNewRemoteFileWriteError(CommonFile file) {
        infoInput.setError("Fail to write remote file: " + file.name());
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

    // ---------- DELETE
    private void deleteRemoteFileAsync(CommonFile file) {
        final Consumer<Result<Nothing>> resultConsumer = result -> {
            if (result instanceof Result.Success<Nothing>) {
                Platform.runLater(() -> onRemoteFileDeleted(file));
            }
            else {
                Platform.runLater(() -> onRemoteFileDeleteError(file));
            }
        };
        final Runnable runnable = () -> {
            try {
                final Result<Nothing> result = service.deleteFile(file);
                resultConsumer.accept(result);
            }
            catch (RemoteException e) {
                e.printStackTrace();
                resultConsumer.accept(Result.Failure.of(e));
            }
        };
        final var thread = new Thread(runnable);

        thread.start();
    }

    private void onRemoteFileDeleted(CommonFile file) {
        infoInput.end("Delete remote file: " + file.path().value());
    }

    private void onRemoteFileDeleteError(CommonFile file) {
        infoInput.setError("Fail to delete remote file: " + file.path()
                                                                .value());
    }

    record DependencyConfig(
        TextFileRepository repository,
        Files.Input filesInput,
        Editor.Input editorInput,
        Info.Input infoInput
    ) {}
}
