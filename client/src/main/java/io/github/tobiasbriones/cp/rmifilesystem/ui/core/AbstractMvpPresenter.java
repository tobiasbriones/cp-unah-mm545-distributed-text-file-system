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

package io.github.tobiasbriones.cp.rmifilesystem.ui.core;

import java.util.Optional;

public abstract class AbstractMvpPresenter<O> implements MvpPresenter<O> {
    private O output;

    protected AbstractMvpPresenter() {
        output = null;
    }

    protected Optional<O> getOutput() {
        return Optional.ofNullable(output);
    }

    @Override
    public final void setOutput(O value) {
        output = value;
    }
}
