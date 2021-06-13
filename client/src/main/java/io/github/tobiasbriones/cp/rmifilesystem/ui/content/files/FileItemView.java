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

package io.github.tobiasbriones.cp.rmifilesystem.ui.content.files;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * @author Tobias Briones
 */
final class FileItemView extends HBox {
    private final Label label;

    FileItemView() {
        super();
        label = new Label();

        init();
    }

    void setName(String name) {
        label.setText(name);
    }

    private void init() {
        setPadding(new Insets(8));
        getChildren().addAll(label);
    }
}
