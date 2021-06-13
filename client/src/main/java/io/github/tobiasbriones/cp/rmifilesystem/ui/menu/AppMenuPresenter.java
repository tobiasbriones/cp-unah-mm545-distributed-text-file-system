package io.github.tobiasbriones.cp.rmifilesystem.ui.menu;

import io.github.tobiasbriones.cp.rmifilesystem.ui.core.AbstractMvpPresenter;
import javafx.application.Platform;

final class AppMenuPresenter extends AbstractMvpPresenter<AppMenu.Output> implements AppMenu.Presenter {
    private final AppMenu.View view;

    AppMenuPresenter(AppMenu.View view) {
        super();
        this.view = view;
    }

    @Override
    public void init() {
        view.createView();
        view.bindEvents(this);
    }

    @Override
    public void onSave() {
        getOutput().ifPresent(AppMenu.DefaultOutput::onSave);
    }

    @Override
    public void onClose() {
        getOutput().ifPresent(AppMenu.DefaultOutput::onClose);
    }

    @Override
    public void onLogin() {
        getOutput().ifPresent(AppMenu.DefaultOutput::onLogin);
    }

    @Override
    public void onNewFile() {
        // TODO
    }

    @Override
    public void onNewDirectory() {
        // TODO
    }

    @Override
    public void onQuit() {
        Platform.exit();
    }

    @Override
    public void onAbout() {
        // TODO
    }
}
