package io.github.tobiasbriones.cp.rmifilesystem.ui.content.files;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;

import java.io.File;

final class FilesPresenter extends AbstractMvpPresenter<Void> implements Files.Presenter {
    private final Files.View view;

    FilesPresenter(Files.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.setController(this);
        view.createView();

        view.addItem(new File("item1"));
        view.addItem(new File("item2"));
    }

    @Override
    public void onItemClick(File file) {
        System.out.println(file);
    }
}
