package io.github.tobiasbriones.cp.rmifilesystem.ui.header;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;

final class HeaderPresenter extends AbstractMvpPresenter<Void> implements Header.Presenter {
    private final Header.View view;

    HeaderPresenter(Header.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.createView();
    }
}
