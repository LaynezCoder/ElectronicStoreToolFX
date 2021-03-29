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
package com.laynezcoder.estfx.alerts;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.constants.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AlertsBuilder {

    private static String title;
    
    private static String buttonStyle;
    
    private static String titleStyle;
    
    private static String bodyStyle;
    
    private static JFXDialog dialog;

    public static void create(AlertType type, StackPane dialogContainer, Node nodeToBlur, Node nodeToDisable, String body) {
        setFunction(type);

        AnchorPane root = new AnchorPane();
        root.setPrefSize(390, 230);

        JFXButton button = new JFXButton("Okey");
        button.getStyleClass().add(buttonStyle);

        HBox buttonContainer = new HBox();
        buttonContainer.setLayoutY(115);
        buttonContainer.setPrefSize(390, 115);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(button);

        Text textTitle = new Text(title);
        textTitle.getStyleClass().add(titleStyle);

        Text textBody = new Text(body);
        textBody.getStyleClass().add(bodyStyle);

        VBox textContainer = new VBox();
        textContainer.setSpacing(5);
        textContainer.setPrefSize(390, 115);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        textContainer.setPadding(new Insets(0, 0, 0, 30));
        textContainer.getChildren().addAll(textTitle, textBody);
        root.getChildren().addAll(buttonContainer, textContainer);

        nodeToDisable.setDisable(true);
        nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);

        dialog = new JFXDialog();
        dialog.setContent(root);
        dialog.setDialogContainer(dialogContainer);
        dialog.setBackground(Background.EMPTY);
        dialog.getStyleClass().add("jfx-dialog-overlay-pane");
        dialog.setTransitionType(DatabaseHelper.dialogTransition());
        dialog.show();

        button.setOnMouseClicked(e -> {
            dialog.close();
        });

        dialog.setOnDialogOpened(e -> {
            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);
        });

        dialog.setOnDialogClosed(e -> {
            nodeToDisable.setDisable(false);
            nodeToBlur.setEffect(null);
        });
    }
    
    public static void close() {
        if (dialog != null) {
            dialog.close();
        }
    }

    private static void setFunction(AlertType type) {
        switch (type) {
            case SUCCES:
                title = "Succes!";
                buttonStyle = "alert-success-button";
                titleStyle = "alert-success-title";
                bodyStyle = "alert-success-body";
            break;
            
            case ERROR:
                title = "Oops!";
                buttonStyle = "alert-error-button";
                titleStyle = "alert-error-title";
                bodyStyle = "alert-error-body";
            break;
        }
    }
}
