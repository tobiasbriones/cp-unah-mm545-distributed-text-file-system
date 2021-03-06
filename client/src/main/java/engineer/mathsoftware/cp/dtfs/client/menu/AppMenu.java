// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.menu;

import engineer.mathsoftware.cp.dtfs.mvp.Initializable;
import engineer.mathsoftware.cp.dtfs.mvp.MvpPresenter;
import engineer.mathsoftware.cp.dtfs.mvp.MvpView;
import javafx.scene.Node;

import java.io.File;

/**
 * @author Tobias Briones
 */
public final class AppMenu implements Initializable {
    public interface Output extends DefaultOutput {
        void onCreateNewFile(File file);

        void onCreateNewDirectory(File dirFile);

        void onLogin(String clientName);

        void onQuit();
    }

    interface DefaultOutput {
        void onSave();

        void onClose();
    }

    interface Controller extends DefaultOutput {
        void onNewFile();

        void onNewDirectory();

        void onLogin();

        void onQuit();

        void onAbout();
    }

    interface View extends MvpView<Controller> {}

    interface Presenter extends MvpPresenter<Output>, Controller {}

    private final View view;
    private final Presenter presenter;

    public AppMenu() {
        view = new AppMenuView();
        presenter = new AppMenuPresenter(view);
    }

    public Node getView() {
        return view.getView();
    }

    public void setOutput(Output output) {
        presenter.setOutput(output);
    }

    public void init() {
        presenter.init();
    }
}
