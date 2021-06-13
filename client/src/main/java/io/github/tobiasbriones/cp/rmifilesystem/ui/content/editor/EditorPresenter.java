package io.github.tobiasbriones.cp.rmifilesystem.ui.content.editor;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;

final class EditorPresenter extends AbstractMvpPresenter<Void> implements Editor.Presenter {
    private final Editor.View view;

    EditorPresenter(Editor.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.createView();
    }

    @Override
    public void onSave() {
        // TODO
    }
}
