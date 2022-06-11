// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.mvp;

import javafx.scene.Node;

/**
 * @author Tobias Briones
 */
public interface MvpView<C> {
    Node getView();

    default void bindEvents(C controller) {}

    void createView();
}
