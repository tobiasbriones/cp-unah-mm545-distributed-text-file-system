package io.github.tobiasbriones.cp.rmifilesystem.ui.menu;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpView;
import javafx.scene.Node;

import java.io.File;

public final class AppMenu implements Initializable {
    public interface Output extends DefaultOutput {
        void onCreateNewFile(File file);

        void onCreateNewDirectory(File dirFile);
    }

    interface DefaultOutput {
        void onSave();

        void onClose();

        void onLogin();
    }

    interface Controller extends DefaultOutput {
        void onNewFile();

        void onNewDirectory();

        void onQuit();

        void onAbout();
    }

    interface View extends MvpView<Controller> {}

    interface Presenter extends MvpPresenter<Output>, Controller {}

    private final View view;
    private final Presenter presenter;

    public AppMenu() {
        view = new AppMenuView();
        presenter = new AppMenuPresenter(view);
    }

    public Node getView() {
        return view.getView();
    }

    public void setOutput(Output output) {
        presenter.setOutput(output);
    }

    public void init() {
        presenter.init();
    }
}
