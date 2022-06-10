// Copyright (c) 2021 Tobias Briones. All rights reserved.
// SPDX-License-Identifier: BSD-3-Clause
// This file is part of https://github.com/tobiasbriones/cp-unah-mm545-distributed-text-file-system

package engineer.mathsoftware.cp.dtfs.mvp;

import java.util.Optional;

/**
 * @author Tobias Briones
 */
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
