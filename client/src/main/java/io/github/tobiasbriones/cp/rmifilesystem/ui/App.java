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

package io.github.tobiasbriones.cp.rmifilesystem.ui;

import io.github.tobiasbriones.cp.rmifilesystem.ui.content.Content;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpView;
import io.github.tobiasbriones.cp.rmifilesystem.ui.header.Header;
import io.github.tobiasbriones.cp.rmifilesystem.ui.menu.AppMenu;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class App implements Initializable {
    private static final double MIN_WIDTH = 480.0d;

    interface Presenter extends MvpPresenter<Void> {}

    interface View extends MvpView<Void> {}

    record ChildrenConfig(
        AppMenu menu,
        Header header,
        Content content
    ) {
        ViewConfig newViewConfig() {
            return new ViewConfig(
                menu.getView(),
                header.getView(),
                content.getView()
            );
        }
    }

    record ViewConfig(
        Node menuView,
        Node headerView,
        Node contentView
    ) {}

    public static App newInstance() {
        final var menu = new AppMenu();
        final var header = new Header();
        final var childrenConfig = new ChildrenConfig(
            menu,
            header,
            Content.newInstance()
        );
        return new App(childrenConfig);
    }

    private final View view;
    private final Presenter presenter;
    private final AppMenu menu;
    private final AppMenuOutput menuOutput;
    private final Header header;
    private final Content content;

    private App(ChildrenConfig childrenConfig) {
        view = new AppView(childrenConfig.newViewConfig());
        menu = childrenConfig.menu();
        menuOutput = new AppMenuOutput();
        header = childrenConfig.header();
        content = childrenConfig.content();
        presenter = new AppPresenter(view, header.getInput());
    }

    public void start(Stage stage) {
        final var scene = new Scene((Parent) view);
        final var title = "JavaRMI Text File System";

        menu.setOutput(menuOutput);
        init();

        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);
        stage.show();
    }

    @Override
    public void init() {
        presenter.init();
        menu.init();
        header.init();
        content.init();
    }
}
