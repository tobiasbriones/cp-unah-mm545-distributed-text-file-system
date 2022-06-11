// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.content.files;

import engineer.mathsoftware.cp.dtfs.client.AppLocalFiles;
import engineer.mathsoftware.cp.dtfs.io.CommonFile;
import engineer.mathsoftware.cp.dtfs.io.CommonPath;
import engineer.mathsoftware.cp.dtfs.io.Directory;
import engineer.mathsoftware.cp.dtfs.io.File;
import engineer.mathsoftware.cp.dtfs.io.file.Nothing;
import engineer.mathsoftware.cp.dtfs.io.file.Result.Failure;
import engineer.mathsoftware.cp.dtfs.io.file.Result.Success;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileContent;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileRepository;
import engineer.mathsoftware.cp.dtfs.io.node.DirectoryNode;
import engineer.mathsoftware.cp.dtfs.io.node.FileNode;
import engineer.mathsoftware.cp.dtfs.io.node.FileSystem;
import engineer.mathsoftware.cp.dtfs.io.node.Node;
import engineer.mathsoftware.cp.dtfs.mvp.AbstractMvpPresenter;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;
import java.util.HashSet;
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
        var pathValue = view.getCreateInputText();
        var path = CommonPath.of(pathValue);

        if (path.isEmpty()) {
            return;
        }
        if (pathValue.endsWith(".txt")) {
            createNewFile(new File.TextFile(path.get()));
        }
        else {
            createNewDir(new Directory(path.get()));
        }
    }

    @Override
    public void onItemClick(CommonFile file) {
        getOutput().ifPresent(output -> {
            view.setCreateInputText(file.path().value());

            if (file instanceof File.TextFile f) {
                output.onOpenFile(f);
            }
        });
    }

    @Override
    public void onNewFileAction(DirectoryNode node) {
        var newFileName = showInputDialog("Create new file");

        if (!newFileName.endsWith(".txt") || newFileName.contains(CommonPath.SEPARATOR)) {
            return;
        }
        var path = CommonPath.of(
            node.commonPath(),
            new CommonPath(newFileName)
        );
        var file = new File.TextFile(path);
        createNewFile(file);
    }

    @Override
    public void onNewDirectoryAction(DirectoryNode node) {
        var newDirName = showInputDialog("Create new directory");
        var path = CommonPath.of(
            node.commonPath(),
            new CommonPath(newDirName)
        );
        var directory = Directory.of(path);
        createNewDir(directory);
    }

    @Override
    public void onDeleteAction(Node<?> node) {
        if (node instanceof FileNode fn && fn.commonFile() instanceof File.TextFile f) {
            getOutput().ifPresent(output -> output.onCloseFile(f));
        }
        delete(node.commonFile());
    }

    @Override
    public FileSystem.Status getStatus(File file) {
        Supplier<FileSystem.Status> defaultSupplier =
            () -> new FileSystem.Status(
                file,
                true
            );

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

    private void createNewFile(File.TextFile file) {
        switch (repository.add(new TextFileContent(file, ""))) {
            case Success<Nothing> ignored -> getOutput().ifPresent(
                output -> output.onFileCreated(file)
            );
            case Failure<Nothing> fail -> fail.ifPresent(System.out::println);
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
        switch (commonFile) {
            case Directory d -> deleteDirectory(d);
            case File.TextFile f -> deleteFile(f);
        }
    }

    private void deleteDirectory(Directory d) {
        try {
            AppLocalFiles.deleteDirectory(d);
            getOutput().ifPresent(output -> output.onFileDeleted(d));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile(File.TextFile f) {
        switch (repository.remove(f)) {
            case Success<Nothing> ignored -> getOutput().ifPresent(
                output -> output.onFileDeleted(f)
            );
            case Failure<Nothing> fail -> fail.ifPresent(System.out::println);
        }
    }

    private static String showInputDialog(String msg) {
        var dialog = new TextInputDialog("");
        dialog.setHeaderText(msg);
        dialog.showAndWait();
        return dialog.getEditor().getText();
    }
}
