package io.github.tobiasbriones.cp.rmifilesystem.ui.header;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpView;
import javafx.scene.Node;

public final class Header implements Initializable {
    public interface Input {
        void setUser(String value);
    }

    interface View extends MvpView<Void>, Input {}

    interface Presenter extends MvpPresenter<Void> {}

    private final View view;
    private final Presenter presenter;

    public Header() {
        view = new HeaderView();
        presenter = new HeaderPresenter(view);
    }

    public Node getView() {
        return view.getView();
    }

    public Input getInput() {
        return view;
    }

    public void init() {
        presenter.init();
    }
}
