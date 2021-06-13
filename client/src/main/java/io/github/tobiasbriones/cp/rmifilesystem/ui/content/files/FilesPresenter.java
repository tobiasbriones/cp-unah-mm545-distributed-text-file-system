package io.github.tobiasbriones.cp.rmifilesystem.ui.content.files;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;

final class FilesPresenter extends AbstractMvpPresenter<Void> implements Files.Presenter {
    private final Files.View view;

    FilesPresenter(Files.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.createView();
    }
}
