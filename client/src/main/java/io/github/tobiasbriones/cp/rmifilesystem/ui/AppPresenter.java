package io.github.tobiasbriones.cp.rmifilesystem.ui;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;

final class AppPresenter extends AbstractMvpPresenter<Void> implements App.Presenter {
    private final App.View view;

    AppPresenter(App.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.createView();
    }
}
