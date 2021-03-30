/*
 * Copyright 2020-2021 LaynezCode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laynezcoder.estfx.util;

import com.jfoenix.controls.JFXPopup;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

public class ContextMenu {

    MaterialDesignIconView ICON_EDIT = new MaterialDesignIconView(MaterialDesignIcon.PENCIL);

    MaterialDesignIconView ICON_DELETE = new MaterialDesignIconView(MaterialDesignIcon.CLOSE);

    MaterialDesignIconView ICON_DETAILS = new MaterialDesignIconView(MaterialDesignIcon.CLIPBOARD_OUTLINE);

    MaterialDesignIconView ICON_REFRESH = new MaterialDesignIconView(MaterialDesignIcon.REFRESH);

    private final JFXPopup popup;

    private final Node node;

    private Button edit;

    private Button delete;

    private Button details;

    private Button refresh;

    public ContextMenu(Node node) {
        this.node = node;

        popup = new JFXPopup();
        popup.setPopupContent(getContent());
        popup.getStyleClass().add("jfx-popup");
    }

    public void setActionEdit(EventHandler action) {
        edit.setOnAction(action);
    }

    public void setActionDelete(EventHandler action) {
        delete.setOnAction(action);
    }

    public void setActionDetails(EventHandler action) {
        details.setOnAction(action);
    }

    public void setActionRefresh(EventHandler action) {
        refresh.setOnAction(action);
    }

    public void show() {
        node.setOnMouseClicked(ev -> {
            if (ev.getButton().equals(MouseButton.SECONDARY)) {
                popup.show(node);
                popup.setAnchorX(ev.getScreenX());
                popup.setAnchorY(ev.getScreenY());
            }
        });
    }

    public void hide() {
        popup.hide();
    }

    public Button getEditButton() {
        return edit;
    }
    
    public Button getDeleteButton() {
        return delete;
    }

    private VBox getContent() {
        setStyleToIcons();
        
        edit = new Button("Edit");
        edit.setGraphic(ICON_EDIT);
        edit.setAlignment(Pos.CENTER_LEFT);
        edit.setContentDisplay(ContentDisplay.LEFT);
        setStyleToButton(edit);

        delete = new Button("Delete");
        delete.setGraphic(ICON_DELETE);
        delete.setAlignment(Pos.CENTER_LEFT);
        delete.setContentDisplay(ContentDisplay.LEFT);
        setStyleToButton(delete);

        details = new Button("Details");
        details.setGraphic(ICON_DETAILS);
        details.setAlignment(Pos.CENTER_LEFT);
        details.setContentDisplay(ContentDisplay.LEFT);
        setStyleToButton(details);

        refresh = new Button("Refresh");
        refresh.setGraphic(ICON_REFRESH);
        refresh.setAlignment(Pos.CENTER_LEFT);
        refresh.setContentDisplay(ContentDisplay.LEFT);
        setStyleToButton(refresh);
       
        VBox contextMenu = new VBox();
        contextMenu.setPadding(new Insets(5));
        contextMenu.getStyleClass().add("card");
        contextMenu.getChildren().addAll(edit, delete, details, refresh);

        return contextMenu;
    }

    private void setStyleToButton(Button button) {
        button.getStyleClass().add("button-context-menu");
    }
    
    private void setStyleToIcons() {
        ICON_DELETE.getStyleClass().add("icon-context-menu");
        ICON_EDIT.getStyleClass().add("icon-context-menu");
        ICON_DETAILS.getStyleClass().add("icon-context-menu");
        ICON_REFRESH.getStyleClass().add("icon-context-menu");
    }
}
