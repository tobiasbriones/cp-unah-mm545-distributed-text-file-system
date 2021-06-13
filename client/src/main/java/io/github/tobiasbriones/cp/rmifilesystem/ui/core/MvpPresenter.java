package io.github.tobiasbriones.cp.rmifilesystem.ui.core;

public interface MvpPresenter<O> extends Initializable {
    void setOutput(O value);
}
