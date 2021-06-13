package io.github.tobiasbriones.cp.rmifilesystem.ui.content.files;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpView;
import javafx.scene.Node;

public final class Files implements Initializable {
    interface View extends MvpView<Void> {}

    interface Presenter extends MvpPresenter<Void> {}

    public static Files newInstance() {
        return new Files();
    }

    private final View view;
    private final Presenter presenter;

    private Files() {
        view = new FilesView();
        presenter = new FilesPresenter(view);
    }

    public Node getView() {
        return view.getView();
    }

    @Override
    public void init() {
        presenter.init();
    }
}
