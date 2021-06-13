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
