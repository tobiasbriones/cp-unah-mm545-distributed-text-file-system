// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.content.editor;

import engineer.mathsoftware.cp.dtfs.io.File;
import engineer.mathsoftware.cp.dtfs.io.file.text.TextFileRepository;
import engineer.mathsoftware.cp.dtfs.mvp.Initializable;
import engineer.mathsoftware.cp.dtfs.mvp.MvpPresenter;
import engineer.mathsoftware.cp.dtfs.mvp.MvpView;
import javafx.scene.Node;

/**
 * @author Tobias Briones
 */
public final class Editor implements Initializable {
    public record DependencyConfig(TextFileRepository repository) {}

    public interface Input {
        void setWorkingFile(File.TextFile file, String content);

        void closeFile(File.TextFile file);

        void update();
    }

    public interface Output {
        void onFileAddedToChangelist(File file);

        void onPush(File file);

        void onPull(File file);
    }

    interface Controller {
        void onPushButtonClick();

        void onPullButtonClick();

        void onSaveButtonClick();
    }

    interface View extends MvpView<Controller> {
        String getContent();

        void setContent(String value);

        void setWorkingFile(String fileName);
    }

    interface Presenter extends MvpPresenter<Output>,
                                Controller,
                                Input {}

    private final View view;
    private final Presenter presenter;

    public Editor(DependencyConfig config) {
        view = new EditorView();
        presenter = new EditorPresenter(view, config.repository());
    }

    public Node getView() {
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
