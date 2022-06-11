// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.info;

import engineer.mathsoftware.cp.dtfs.mvp.Initializable;
import engineer.mathsoftware.cp.dtfs.mvp.MvpPresenter;
import engineer.mathsoftware.cp.dtfs.mvp.MvpView;
import javafx.scene.Node;

/**
 * @author Tobias Briones
 */
public final class Info implements Initializable {
    public interface Input {
        void setError(String value);

        void start(String value);

        void end(String value);
    }

    interface View extends MvpView<Void>,
                           Input {}

    interface Presenter extends MvpPresenter<Void> {}

    private final View view;
    private final Presenter presenter;

    public Info() {
        view = new InfoView();
        presenter = new InfoPresenter(view);
    }

    public Node getView() {
        return view.getView();
    }

    public Input getInput() {
        return view;
    }

    public void init() {
        presenter.init();
    }
}
