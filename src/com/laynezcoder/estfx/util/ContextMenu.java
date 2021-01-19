package com.laynezcoder.estfx.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.laynezcoder.estfx.resources.Constants;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

public class ContextMenu {

    MaterialDesignIcon ICON_EDIT = MaterialDesignIcon.TABLE_EDIT;

    MaterialDesignIcon ICON_DELETE = MaterialDesignIcon.DELETE_VARIANT;

    MaterialDesignIcon ICON_DETAILS = MaterialDesignIcon.INFORMATION_OUTLINE;

    MaterialDesignIcon ICON_REFRESH = MaterialDesignIcon.REFRESH;

    private final JFXPopup popup;

    private final Node node;

    private JFXButton edit;

    private JFXButton delete;

    private JFXButton details;

    private JFXButton refresh;

    public ContextMenu(Node node) {
        this.node = node;

        popup = new JFXPopup();
        popup.setPopupContent(getContent());
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

    public JFXButton getEditButton() {
        return edit;
    }
    
    public JFXButton getDeleteButton() {
        return delete;
    }

    private VBox getContent() {
        edit = new JFXButton("Edit");
        edit.setGraphic(new MaterialDesignIconView(ICON_EDIT));
        edit.setAlignment(Pos.BASELINE_LEFT);
        edit.setContentDisplay(ContentDisplay.LEFT);
        style(edit);

        delete = new JFXButton("Delete");
        delete.setGraphic(new MaterialDesignIconView(ICON_DELETE));
        delete.setAlignment(Pos.BASELINE_LEFT);
        delete.setContentDisplay(ContentDisplay.LEFT);
        style(delete);

        details = new JFXButton("Details");
        details.setGraphic(new MaterialDesignIconView(ICON_DETAILS));
        details.setAlignment(Pos.BASELINE_LEFT);
        details.setContentDisplay(ContentDisplay.LEFT);
        style(details);

        refresh = new JFXButton("Refresh");
        refresh.setGraphic(new MaterialDesignIconView(ICON_REFRESH));
        refresh.setAlignment(Pos.BASELINE_LEFT);
        refresh.setContentDisplay(ContentDisplay.LEFT);
        style(refresh);

        VBox contextMenu = new VBox();
        contextMenu.setPrefSize(100, 130);
        contextMenu.getChildren().addAll(edit, delete, details, refresh);

        return contextMenu;
    }

    private void style(JFXButton button) {
        button.getStylesheets().add(Constants.CSS_LIGHT_THEME);
        button.getStyleClass().add("button-context-menu");
    }
}
