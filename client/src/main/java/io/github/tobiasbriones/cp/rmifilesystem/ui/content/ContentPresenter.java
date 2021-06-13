package io.github.tobiasbriones.cp.rmifilesystem.ui.content;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;

final class ContentPresenter extends AbstractMvpPresenter<Void> implements Content.Presenter {
    private final Content.View view;

    ContentPresenter(Content.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.createView();
    }
}
