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

package engineer.mathsoftware.cp.dtfs.client.info;

import com.github.tobiasbriones.cp.rmifilesystem.mvp.Initializable;
import com.github.tobiasbriones.cp.rmifilesystem.mvp.MvpPresenter;
import com.github.tobiasbriones.cp.rmifilesystem.mvp.MvpView;
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

    interface View extends MvpView<Void>, Input {}

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
