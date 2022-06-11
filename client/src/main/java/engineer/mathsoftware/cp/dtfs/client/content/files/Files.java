// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.content.files;

import engineer.mathsoftware.cp.dtfs.io.CommonFile;
import engineer.mathsoftware.cp.dtfs.io.File;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileRepository;
import engineer.mathsoftware.cp.dtfs.io.node.DirectoryNode;
import engineer.mathsoftware.cp.dtfs.io.node.FileSystem;
import engineer.mathsoftware.cp.dtfs.io.node.Node;
import engineer.mathsoftware.cp.dtfs.mvp.Initializable;
import engineer.mathsoftware.cp.dtfs.mvp.MvpPresenter;
import engineer.mathsoftware.cp.dtfs.mvp.MvpView;

/**
 * @author Tobias Briones
 */
public final class Files implements Initializable {
    public record DependencyConfig(TextFileRepository repository) {}

    public interface Input {
        void update();
    }

    public interface Output {
        void onOpenFile(File.TextFile file);

        void onCloseFile(File.TextFile file);

        void onFileCreated(CommonFile file);

        void onFileDeleted(CommonFile file);
    }

    interface Controller {
        void onCreateButtonClick();

        void onItemClick(CommonFile file);

        void onNewFileAction(DirectoryNode node);

        void onNewDirectoryAction(DirectoryNode node);

        void onDeleteAction(Node<?> node);

        FileSystem.Status getStatus(File file); // place this here temporarily

        boolean isInChangelist(File file); // place this here temporarily
    }

    interface View extends MvpView<Controller> {
        String getCreateInputText();

        void setCreateInputText(String text);

        void setController(Controller value);

        void setRoot(DirectoryNode root);

        void clear();
    }

    interface Presenter extends MvpPresenter<Output>,
                                Controller,
                                Input {}

    public static Files newInstance(DependencyConfig config) {
        return new Files(config);
    }

    private final View view;
    private final Presenter presenter;

    private Files(DependencyConfig config) {
        view = new FilesView();
        presenter = new FilesPresenter(view, config.repository());
    }

    public javafx.scene.Node getView() {
        return view.getView();
    }

    public Input getInput() {
        return presenter;
    }

    public void setOutput(Output value) {
        presenter.setOutput(value);
    }

    @Override
    public void init() {
        presenter.init();
    }
}
