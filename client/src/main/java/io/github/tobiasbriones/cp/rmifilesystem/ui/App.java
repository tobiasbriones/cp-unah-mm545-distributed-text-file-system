package io.github.tobiasbriones.cp.rmifilesystem.ui;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpView;
import io.github.tobiasbriones.cp.rmifilesystem.ui.header.Header;
import io.github.tobiasbriones.cp.rmifilesystem.ui.menu.AppMenu;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class App implements Initializable {
    private static final double MIN_WIDTH = 480.0d;

    interface Presenter extends MvpPresenter<Void> {}

    interface View extends MvpView<Void> {}

    record ChildrenConfig(
        AppMenu menu,
        Header header
    ) {
        ViewConfig newViewConfig() {
            return new ViewConfig(
                menu.getView(),
                header.getView()
            );
        }
    }

    record ViewConfig(
        Node menuView,
        Node headerView
    ) {}

    public static App newInstance() {
        final var menu = new AppMenu();
        final var header = new Header();
        final var childrenConfig = new ChildrenConfig(
            menu,
            header
        );
        return new App(childrenConfig);
    }

    private final View view;
    private final Presenter presenter;
    private final AppMenu menu;
    private final AppMenuOutput menuOutput;
    private final Header header;

    private App(ChildrenConfig childrenConfig) {
        view = new AppView(childrenConfig.newViewConfig());
        menu = childrenConfig.menu();
        menuOutput = new AppMenuOutput();
        header = childrenConfig.header();
        presenter = new AppPresenter(view, header.getInput());
    }

    public void start(Stage stage) {
        final var scene = new Scene((Parent) view);
        final var title = "JavaRMI Text File System";

        menu.setOutput(menuOutput);
        init();

        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);
        stage.show();
    }

    @Override
    public void init() {
        presenter.init();
        menu.init();
        header.init();
    }
}
