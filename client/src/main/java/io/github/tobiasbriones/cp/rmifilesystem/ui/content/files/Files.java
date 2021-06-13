package io.github.tobiasbriones.cp.rmifilesystem.ui.content.files;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpView;
import javafx.scene.Node;

import java.io.File;
import java.util.List;

public final class Files implements Initializable {
    interface Controller {
        void onItemClick(File file);
    }

    interface View extends MvpView<Controller> {
        void setController(Controller value);

        void addItem(File file);

        void clear();

        default void addItems(List<? extends File> files) {
            files.forEach(this::addItem);
        }
    }

    interface Presenter extends MvpPresenter<Void>, Controller {}

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
