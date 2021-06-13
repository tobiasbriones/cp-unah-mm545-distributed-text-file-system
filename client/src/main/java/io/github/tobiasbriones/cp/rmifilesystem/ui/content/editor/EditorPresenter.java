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

package io.github.tobiasbriones.cp.rmifilesystem.ui.content.editor;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;

final class EditorPresenter extends AbstractMvpPresenter<Void> implements Editor.Presenter {
    private final Editor.View view;

    EditorPresenter(Editor.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.createView();
    }

    @Override
    public void onSave() {
        // TODO
    }
}
