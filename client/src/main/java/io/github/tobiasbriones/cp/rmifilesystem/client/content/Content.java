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

import io.github.tobiasbriones.cp.rmifilesystem.model.OnFileUpdateListener;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileRepository;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystems;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.MvpView;
import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;
import io.github.tobiasbriones.cp.rmifilesystem.client.content.editor.Editor;
import io.github.tobiasbriones.cp.rmifilesystem.client.content.files.Files;
import io.github.tobiasbriones.cp.rmifilesystem.client.AppLocalFiles;
import javafx.scene.Node;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import static io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService.*;
import static io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem.*;

/**
 * @author Tobias Briones
 */
public final class Content implements Initializable {
    interface View extends MvpView<Void> {}

    interface Presenter extends MvpPresenter<Void> {}

    @FunctionalInterface
    interface OnLocalFsChangeListener {
        void update();
    }

    public static Content newInstance() {
        final TextFileRepository repository = AppLocalFiles.newTextFileRepository();
        final var config = new ChildrenConfig(
            Files.newInstance(
                new Files.DependencyConfig(repository)
            ),
            Editor.newInstance(
                new Editor.DependencyConfig(repository)
            )
        );
        return new Content(config, repository);
    }

    record ChildrenConfig(
        Files files,
        Editor editor
    ) {
        ViewConfig newViewConfig() {
            return new ViewConfig(
                files.getView(),
                editor.getView()
            );
        }
    }

    record ViewConfig(
        Node filesView,
        Node editorView
    ) {}

    private final View view;
    private final Presenter presenter;
    private final Files files;
    private final Editor editor;
    private final FilesOutput filesOutput;
    private final EditorOutput editorOutput;
    private final OnLocalFsChangeListener l;
    private FileSystemService service;
    private OnFileUpdateListener client;

    private Content(
        ChildrenConfig config,
        TextFileRepository repository
    ) {
        view = new ContentView(config.newViewConfig());
        presenter = new ContentPresenter(view);
        files = config.files();
        editor = config.editor();
        filesOutput = new FilesOutput(files.getInput(), editor.getInput(), repository);
        editorOutput = new EditorOutput(files.getInput(), editor.getInput(), repository);
        l = this::update;
        service = null;
        client = null;
    }

    public Node getView() {
        return view.getView();
    }

    public void setService(FileSystemService value) {
        service = value;
        filesOutput.setService(value);
        editorOutput.setService(value);
        bindServiceListener();
        updateLocalFs(service); // should be async
        update();
    }

    @Override
    public void init() {
        setOutputs();
        presenter.init();
        files.init();
        editor.init();
    }

    public void unbind() throws RemoteException {
        if (service == null) {
            return;
        }
        service.removeOnFileUpdateListener(client);
        UnicastRemoteObject.unexportObject(client, true);
    }

    private void update() {
        files.getInput().update();
        editor.getInput().update();
    }

    private void setOutputs() {
        files.setOutput(filesOutput);
        editor.setOutput(editorOutput);
    }

    private void bindServiceListener() {
        try {
            client = new ContentOnFileUpdateListener(l);
            service.addOnFileUpdateListener(client);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    static void updateLocalFs(FileSystemService service) {
        try {
            final RealTimeFileSystem system = service.getRealTimeFileSystem();
            final Map<File, LastUpdateStatus> statuses = AppLocalFiles.readStatuses();
            final FileSystem fs = FileSystems.buildFileSystem(system, statuses);

            AppLocalFiles.saveFs(fs);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
