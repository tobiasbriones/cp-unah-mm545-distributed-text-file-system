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

package engineer.mathsoftware.cp.dtfs.client.content.files;

import engineer.mathsoftware.cp.dtfs.client.AppLocalFiles;
import engineer.mathsoftware.cp.dtfs.model.io.CommonFile;
import engineer.mathsoftware.cp.dtfs.model.io.CommonPath;
import engineer.mathsoftware.cp.dtfs.model.io.Directory;
import engineer.mathsoftware.cp.dtfs.model.io.File;
import engineer.mathsoftware.cp.dtfs.model.io.file.Nothing;
import engineer.mathsoftware.cp.dtfs.model.io.file.Result;
import engineer.mathsoftware.cp.dtfs.model.io.file.text.TextFileContent;
import engineer.mathsoftware.cp.dtfs.model.io.file.text.TextFileRepository;
import engineer.mathsoftware.cp.dtfs.model.io.node.DirectoryNode;
import engineer.mathsoftware.cp.dtfs.model.io.node.FileNode;
import engineer.mathsoftware.cp.dtfs.model.io.node.FileSystem;
import engineer.mathsoftware.cp.dtfs.model.io.node.Node;
import engineer.mathsoftware.cp.dtfs.mvp.AbstractMvpPresenter;

import engineer.mathsoftware.cp.dtfs.model.io.File.TextFile;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Tobias Briones
 */
final class FilesPresenter extends AbstractMvpPresenter<Files.Output> implements Files.Presenter {
    private final Files.View view;
    private final TextFileRepository repository;
    private Set<File> changelist;
    private FileSystem fs;

    FilesPresenter(Files.View view, TextFileRepository repository) {
        super();
        this.view = view;
        this.repository = repository;
        changelist = new HashSet<>(0);
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
        final String pathValue = view.getCreateInputText();
        final Optional<CommonPath> path = CommonPath.of(pathValue);

        if (path.isEmpty()) {
            return;
        }
        if (pathValue.endsWith(".txt")) {
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

        if (!newFileName.endsWith(".txt") || newFileName.contains(CommonPath.SEPARATOR)) {
            return;
        }
        final CommonPath path = CommonPath.of(node.commonPath(), new CommonPath(newFileName));
        final TextFile file = new File.TextFile(path);

        createNewFile(file);
    }

    @Override
    public void onNewDirectoryAction(DirectoryNode node) {
        final String newDirName = showInputDialog("Create new directory");
        final CommonPath path = CommonPath.of(node.commonPath(), new CommonPath(newDirName));
        final Directory directory = Directory.of(path);

        createNewDir(directory);
    }

    @Override
    public void onDeleteAction(Node<?> node) {
        if (node instanceof FileNode fn && fn.commonFile() instanceof TextFile f) {
            getOutput().ifPresent(output -> output.onCloseFile(f));
        }
        delete(node.commonFile());
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
    public boolean isInChangelist(File file) {
        return changelist.contains(file);
    }

    @Override
    public void update() {
        try {
            fs = AppLocalFiles.readFs();
            changelist = AppLocalFiles.readChangelist();

            view.clear();
            view.setRoot(fs.getRoot());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewFile(TextFile file) {
        final var result = repository.add(new TextFileContent(file, ""));

        if (result instanceof Result.Failure<Nothing> f) {
            f.ifPresent(System.out::println);
        }
        else {
            getOutput().ifPresent(output -> output.onFileCreated(file));
        }
    }

    private void createNewDir(Directory directory) {
        try {
            AppLocalFiles.createDirectory(directory);
            getOutput().ifPresent(output -> output.onFileCreated(directory));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delete(CommonFile commonFile) {
        if (commonFile instanceof Directory d) {
            try {
                AppLocalFiles.deleteDirectory(d);
                getOutput().ifPresent(output -> output.onFileDeleted(d));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (commonFile instanceof TextFile f){
            final var result = repository.remove(f);

            if (result instanceof Result.Failure<Nothing> fail) {
                fail.ifPresent(System.out::println);
            }
            else {
                getOutput().ifPresent(output -> output.onFileDeleted(f));
            }
        }
    }

    private static String showInputDialog(String msg) {
        final var dialog = new TextInputDialog("");

        dialog.setHeaderText(msg);
        dialog.showAndWait();
        return dialog.getEditor().getText();
    }
}
