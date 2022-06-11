// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.client.header;

import engineer.mathsoftware.cp.dtfs.mvp.Initializable;
import engineer.mathsoftware.cp.dtfs.mvp.MvpPresenter;
import engineer.mathsoftware.cp.dtfs.mvp.MvpView;
import javafx.scene.Node;

/**
 * @author Tobias Briones
 */
public final class Header implements Initializable {
    public interface Input {
        void setUser(String value);

        void setConnected(String host);

        void setStatus(String value);

        void setHost(String value);
    }

    interface View extends MvpView<Void>,
                           Input {}

    interface Presenter extends MvpPresenter<Void> {}

    private final View view;
    private final Presenter presenter;

    public Header() {
        view = new HeaderView();
        presenter = new HeaderPresenter(view);
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
