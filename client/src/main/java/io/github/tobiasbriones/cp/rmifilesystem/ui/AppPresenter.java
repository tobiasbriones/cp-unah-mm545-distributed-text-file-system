package io.github.tobiasbriones.cp.rmifilesystem.ui;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.header.Header;

final class AppPresenter extends AbstractMvpPresenter<Void> implements App.Presenter {
    private final App.View view;
    private final Header.Input headerInput;

    AppPresenter(App.View view, Header.Input headerInput) {
        super();
        this.view = view;
        this.headerInput = headerInput;
    }

    @Override
    public void init() {
        view.createView();
        headerInput.setUser("Unknown user");
    }
}
