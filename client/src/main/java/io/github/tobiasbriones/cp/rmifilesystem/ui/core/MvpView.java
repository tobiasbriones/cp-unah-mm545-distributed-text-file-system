package io.github.tobiasbriones.cp.rmifilesystem.ui.core;

import javafx.scene.Node;

public interface MvpView<C> {
    Node getView();

    void createView();

    default void bindEvents(C controller) {}
}
