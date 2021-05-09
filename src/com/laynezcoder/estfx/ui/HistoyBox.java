/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laynezcoder.estfx.ui;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class HistoyBox extends HBox {

    private final MaterialDesignIcon ICON = MaterialDesignIcon.TOOLTIP_OUTLINE_PLUS;

    private String message;

    public enum Module {
        CUSTOMER, QUOTE, PRODUCT, SALE
    }

    public HistoyBox(Module module, String date, String value) {
        setModule(module, date, value);

        MaterialDesignIconView icon = new MaterialDesignIconView(ICON);
        icon.getStyleClass().add("icon-close");

        Text content = new Text(message);
        content.getStyleClass().add("sub-title");

        TextFlow textContainer = new TextFlow(content);
        HBox.setHgrow(textContainer, Priority.ALWAYS);

        setSpacing(10);
        setPadding(new Insets(20));
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().addAll("card", "card-user");
        getChildren().addAll(icon, textContainer);
    }

    private void setModule(Module module, String date, String value) {
        String content = "Added a " + module.name().toLowerCase() + " on " + date + " with the ";

        switch (module) {
            case CUSTOMER:
                message = content + "name of " + value;
                break;
            case QUOTE:
                message = content + "description of " + value;
                break;
            case PRODUCT:
                message = content + "name of " + value;
                break;
            case SALE:
                message = content + "value of " + value;
                break;
        }
    }
}
