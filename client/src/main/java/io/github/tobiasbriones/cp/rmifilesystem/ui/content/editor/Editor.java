package io.github.tobiasbriones.cp.rmifilesystem.ui.content.editor;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpView;
import javafx.scene.Node;

public final class Editor implements Initializable {
    public interface Input {
        void setContent(String value);
    }

    interface Controller {
        void onSave();
    }

    interface View extends MvpView<Controller>, Input {}

    interface Presenter extends MvpPresenter<Void>, Controller {}

    public static Editor newInstance() {
        return new Editor();
    }

    private final View view;
    private final Presenter presenter;

    private Editor() {
        view = new EditorView();
        presenter = new EditorPresenter(view);
    }

    public Node getView() {
        return view.getView();
    }

    @Override
    public void init() {
        presenter.init();
    }
}
