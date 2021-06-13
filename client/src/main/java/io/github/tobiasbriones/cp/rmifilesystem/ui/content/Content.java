package io.github.tobiasbriones.cp.rmifilesystem.ui.content;

import io.github.tobiasbriones.cp.rmifilesystem.ui.content.editor.Editor;
import io.github.tobiasbriones.cp.rmifilesystem.ui.content.files.Files;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.Initializable;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpPresenter;
import io.github.tobiasbriones.cp.rmifilesystem.ui.core.MvpView;
import javafx.scene.Node;

import java.io.File;

public final class Content implements Initializable {
    interface View extends MvpView<Void> {}

    interface Presenter extends MvpPresenter<Void> {}

    record ChildrenConfig(
       Files files,
       Editor editor
    ) {
        ViewConfig newViewConfig() {
            return new ViewConfig(
                files.getView(),
                editor.getView()
            );
        }
    }

    record ViewConfig(
        Node filesView,
        Node editorView
    ) {}

    public static Content newInstance() {
        final var config = new ChildrenConfig(
            Files.newInstance(),
            Editor.newInstance()
        );
        return new Content(config);
    }

    private final View view;
    private final Presenter presenter;
    private final Files files;
    private final Editor editor;

    private Content(ChildrenConfig config) {
        view = new ContentView(config.newViewConfig());
        presenter = new ContentPresenter(view);
        files = config.files();
        editor = config.editor();
    }

    public Node getView() {
        return view.getView();
    }

    @Override
    public void init() {
        presenter.init();
        files.init();
        editor.init();
    }
}
