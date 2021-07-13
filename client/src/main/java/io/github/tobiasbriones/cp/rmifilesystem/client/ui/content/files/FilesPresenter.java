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

package io.github.tobiasbriones.cp.rmifilesystem.client.ui.content.files;

import io.github.tobiasbriones.cp.rmifilesystem.client.io.AppLocalFiles;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.CommonFile;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.CommonPath;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.Directory;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.DirectoryNode;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.FileSystem;
import io.github.tobiasbriones.cp.rmifilesystem.model.io.node.Node;
import io.github.tobiasbriones.cp.rmifilesystem.mvp.AbstractMvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.model.FileSystemService;

import io.github.tobiasbriones.cp.rmifilesystem.model.io.File.TextFile;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author Tobias Briones
 */
final class FilesPresenter extends AbstractMvpPresenter<Files.Output> implements Files.Presenter {
    private final Files.View view;
    private FileSystemService service;
    private FileSystem fs;

    FilesPresenter(Files.View view) {
        super();
        this.view = view;
        service = null;
        fs = null;
    }

    @Override
    public void init() {
        view.setController(this);
        view.createView();

        update();
    }

    @Override
    public void onCreateButtonClick() {
        final String fileName = view.getCreateInputText();
        final Optional<CommonPath> path = CommonPath.of(fileName);

        if (path.isEmpty()) {
            return;
        }
        if (fileName.endsWith(".txt")) {
            createNewFile(new TextFile(path.get()));
        }
        else {
            createNewDir(new Directory(path.get()));
        }
    }

    @Override
    public void onItemClick(CommonFile file) {
        getOutput().ifPresent(output -> {
            view.setCreateInputText(file.path().value());

            if (file instanceof TextFile f) {
                output.onOpenFile(f);
            }
        });
    }

    @Override
    public void onNewFileAction(DirectoryNode node) {
        final String newFileName = showInputDialog("Create new file");
        System.out.println(newFileName);
    }

    @Override
    public void onNewDirectoryAction(DirectoryNode node) {
        final String newFileName = showInputDialog("Create new directory");
        System.out.println(newFileName);
    }

    @Override
    public void onDeleteAction(Node<?> node) {
        System.out.println("Delete " + node);
    }

    @Override
    public FileSystem.Status getStatus(File file) {
        final Supplier<FileSystem.Status> defaultSupplier = () -> new FileSystem.Status(file, true);

        if (fs == null) {
            return defaultSupplier.get();
        }
        return fs.getStatus(file).orElse(defaultSupplier.get());
    }

    @Override
    public void setService(FileSystemService value) {
        service = value;
    }

    @Override
    public void update() {
        try {
            fs = AppLocalFiles.readFs();

            view.clear();
            view.setRoot(fs.getRoot());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewFile(TextFile file) {
        if (service == null) {
            return;
        }
        try {
            service.writeTextFile(file, "");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewDir(Directory directory) {
        if (service == null) {
            return;
        }
        try {
            service.writeDirectory(directory);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String showInputDialog(String msg) {
        final var dialog = new TextInputDialog("");

        dialog.setHeaderText(msg);
        dialog.showAndWait();
        return dialog.getEditor().getText();
    }
}
