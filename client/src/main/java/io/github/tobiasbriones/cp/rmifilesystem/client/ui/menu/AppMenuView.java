/*
 * Copyright (c) 2021 Tobias Briones. All rights reserved.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 *
 * This file is part of Course Project at UNAH-MM545: Distributed Text File
 * System.
 *
 * This source code is licensed under the BSD-3-Clause License found in the
 * LICENSE file in the root directory of this source tree or at
 * https://opensource.org/licenses/BSD-3-Clause.
 */

package io.github.tobiasbriones.cp.rmifilesystem.client.ui.menu;

import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * @author Tobias Briones
 */
final class AppMenuView extends MenuBar implements AppMenu.View {
    private final Menu fileMenu;
    private final MenuItem fileNewItem;
    private final MenuItem fileNewDirectoryItem;
    private final MenuItem fileSaveItem;
    private final MenuItem fileCloseItem;
    private final MenuItem fileQuitItem;
    private final Menu sessionMenu;
    private final MenuItem sessionLoginItem;
    private final Menu helpMenu;
    private final MenuItem helpAboutItem;

    AppMenuView() {
        super();
        fileMenu = new Menu();
        sessionMenu = new Menu();
        helpMenu = new Menu();
        fileNewItem = new MenuItem();
        fileNewDirectoryItem = new MenuItem();
        fileSaveItem = new MenuItem();
        fileCloseItem = new MenuItem();
        fileQuitItem = new MenuItem();
        sessionLoginItem = new MenuItem();
        helpAboutItem = new MenuItem();
    }

    @Override
    public Node getView() {
        return this;
    }

    @Override
    public void createView() {
        createFileMenu();
        createSessionMenu();
        createHelpMenu();

        getMenus().addAll(
            fileMenu,
            sessionMenu,
            helpMenu
        );
    }

    @Override
    public void bindEvents(AppMenu.Controller controller) {
        fileNewItem.setOnAction(event -> controller.onNewFile());
        fileNewDirectoryItem.setOnAction(event -> controller.onNewDirectory());
        fileSaveItem.setOnAction(event -> controller.onSave());
        fileCloseItem.setOnAction(event -> controller.onClose());
        fileQuitItem.setOnAction(event -> controller.onQuit());

        sessionLoginItem.setOnAction(event -> controller.onLogin());

        helpAboutItem.setOnAction(event -> controller.onAbout());
    }

    private void createFileMenu() {
        fileMenu.setText("File");
        fileNewItem.setText("New File");
        fileNewDirectoryItem.setText("New Directory");
        fileSaveItem.setText("Save");
        fileCloseItem.setText("Close");
        fileQuitItem.setText("Quit");

        fileMenu.getItems().addAll(
            fileNewItem,
            fileNewDirectoryItem,
            new SeparatorMenuItem(),
            fileSaveItem,
            fileCloseItem,
            new SeparatorMenuItem(),
            fileQuitItem
        );
    }

    private void createSessionMenu() {
        sessionMenu.setText("Session");
        sessionLoginItem.setText("Login");

        sessionMenu.getItems().addAll(sessionLoginItem);
    }

    private void createHelpMenu() {
        helpMenu.setText("Help");
        helpAboutItem.setText("About");

        helpMenu.getItems().addAll(helpAboutItem);
    }
}
