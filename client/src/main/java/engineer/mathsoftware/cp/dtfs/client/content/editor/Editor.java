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

package engineer.mathsoftware.cp.dtfs.client.content.editor;

import com.github.tobiasbriones.cp.rmifilesystem.model.io.File;
import com.github.tobiasbriones.cp.rmifilesystem.model.io.file.text.TextFileRepository;
import com.github.tobiasbriones.cp.rmifilesystem.mvp.Initializable;
import com.github.tobiasbriones.cp.rmifilesystem.mvp.MvpPresenter;
import com.github.tobiasbriones.cp.rmifilesystem.mvp.MvpView;
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

        void setWorkingFile(String fileName);

        void setContent(String value);
    }

    interface Presenter extends MvpPresenter<Output>, Controller, Input {}

    public static Editor newInstance(DependencyConfig config) {
        return new Editor(config);
    }

    private final View view;
    private final Presenter presenter;

    private Editor(DependencyConfig config) {
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
