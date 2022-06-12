// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.content;

import engineer.mathsoftware.cp.dtfs.FileSystemService;
import engineer.mathsoftware.cp.dtfs.OnFileUpdateListener;
import engineer.mathsoftware.cp.dtfs.client.AppLocalFiles;
import engineer.mathsoftware.cp.dtfs.client.FileSystemServices;
import engineer.mathsoftware.cp.dtfs.client.content.editor.Editor;
import engineer.mathsoftware.cp.dtfs.client.content.files.Files;
import engineer.mathsoftware.cp.dtfs.client.header.Header;
import engineer.mathsoftware.cp.dtfs.client.info.Info;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileRepository;
import engineer.mathsoftware.cp.dtfs.io.node.FileSystems;
import engineer.mathsoftware.cp.dtfs.mvp.Initializable;
import engineer.mathsoftware.cp.dtfs.mvp.MvpPresenter;
import engineer.mathsoftware.cp.dtfs.mvp.MvpView;
import javafx.application.Platform;
import javafx.scene.Node;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Tobias Briones
 */
public final class Content implements Initializable {
    public record DependencyConfig(
        TextFileRepository repository,
        Header.Input headerInput,
        Info.Input infoInput
    ) {}

    interface View extends MvpView<Void> {}

    interface Presenter extends MvpPresenter<Void> {}

    @FunctionalInterface
    interface OnLocalFsChangeListener {
        void update();
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

    public static Content newInstance(DependencyConfig config) {
        var repository = config.repository();
        var children = new ChildrenConfig(
            new Files(
                new Files.DependencyConfig(repository)
            ),
            new Editor(
                new Editor.DependencyConfig(repository)
            )
        );
        return new Content(config, children);
    }

    private final View view;
    private final Presenter presenter;
    private final Files files;
    private final Editor editor;
    private final FilesOutput filesOutput;
    private final EditorOutput editorOutput;
    private final Header.Input headerInput;
    private final Info.Input infoInput;
    private final OnLocalFsChangeListener l;
    private FileSystemService service;
    private OnFileUpdateListener client;

    private Content(
        DependencyConfig config,
        ChildrenConfig children
    ) {
        view = new ContentView(children.newViewConfig());
        presenter = new ContentPresenter(view);
        headerInput = config.headerInput();
        infoInput = config.infoInput();
        files = children.files();
        editor = children.editor();
        filesOutput = new FilesOutput(new FilesOutput.DependencyConfig(
            config.repository(),
            files.getInput(),
            editor.getInput(),
            infoInput
        ));
        editorOutput = new EditorOutput(new EditorOutput.DependencyConfig(
            config.repository(),
            files.getInput(),
            editor.getInput(),
            infoInput
        ));
        l = this::update;
        service = null;
        client = null;
    }

    public Node getView() {
        return view.getView();
    }

    public void setService(FileSystemService value) {
        service = value;

        bindServiceListener();
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
        infoInput.start("Unbinding service");
        filesOutput.setService(null);
        editorOutput.setService(null);
        service.removeOnFileUpdateListener(client);
        UnicastRemoteObject.unexportObject(client, true);
        infoInput.end("Service unbound");
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
        Runnable runnable = () -> {
            try {
                client = new ContentOnFileUpdateListener(l);

                service.addOnFileUpdateListener(client);
                Platform.runLater(this::onServiceBound);
            }
            catch (RemoteException e) {
                e.printStackTrace();
                Platform.runLater(this::onServiceBindError);
            }
        };
        var thread = new Thread(runnable);

        infoInput.start("Binding service listener");
        thread.start();
    }

    private void onServiceBound() {
        headerInput.setConnected(FileSystemServices.HOST);
        infoInput.end("Service listener bound");
        filesOutput.setService(service);
        editorOutput.setService(service);
        updateLocalFs(service);
        update();
    }

    private void onServiceBindError() {
        infoInput.end("");
        infoInput.setError("Fail to bind service listener");
    }

    static void updateLocalFs(FileSystemService service) {
        try {
            var system = service.getRealTimeFileSystem();
            var statuses = AppLocalFiles.readStatuses();
            var fs = FileSystems.buildFileSystem(system, statuses);
            AppLocalFiles.saveFs(fs);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
