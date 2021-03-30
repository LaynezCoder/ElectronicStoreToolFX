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

import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.constants.Constants;
import com.laynezcoder.estfx.constants.ResourcesPackages;
import com.laynezcoder.estfx.util.JFXDialogTool;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class AlertsBuilder {

    private static Image image;
    private static String titleAlert;
    private static JFXDialogTool dialog;

    public static void create(AlertType type, StackPane dialogContainer, Node nodeToBlur, Node nodeToDisable, String body) {
        function(type);

        MaterialDesignIconView icon = new MaterialDesignIconView(MaterialDesignIcon.CLOSE);
        icon.getStyleClass().add("icon-close");

        Button close = new Button();
        close.setGraphic(icon);
        close.getStyleClass().add("btn-close");

        HBox closeContainer = new HBox(close);
        closeContainer.setAlignment(Pos.TOP_RIGHT);

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(110);
        
        Circle shadow = new Circle(15);
        shadow.setLayoutX(55);
        shadow.setLayoutY(120);
        shadow.getStyleClass().add("shadow-dinosaur-image");
         
        VBox imageContainer = new VBox(imageView, shadow);
        imageContainer.setAlignment(Pos.CENTER);

        Text title = new Text(titleAlert);
        title.getStyleClass().add("h1");

        Text subtitle = new Text(body);
        subtitle.getStyleClass().add("sub-title");

        TextFlow subtitleContainer = new TextFlow(subtitle);
        subtitleContainer.setTextAlignment(TextAlignment.CENTER);

        VBox root = new VBox();
        root.getChildren().addAll(closeContainer, imageContainer, title, subtitleContainer);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("card");
        root.setPrefSize(350, 260);

        nodeToDisable.setDisable(true);
        nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);

        dialog = new JFXDialogTool(root, dialogContainer);
        dialog.show();

        close.setOnMouseClicked(e -> {
            dialog.close();
        });
        
        TranslateTransition transition = Animations.imageTransition(imageView);
        
        dialog.setOnDialogOpened(e -> {
            transition.play();
            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);
        });

        dialog.setOnDialogClosed(e -> {
            transition.pause();
            nodeToDisable.setDisable(false);
            nodeToBlur.setEffect(null);
        });
    }

    public static void close() {
        if (dialog != null) {
            dialog.close();
        }
    }

    private static void function(AlertType type) {
        switch (type) {
            case SUCCES:
                titleAlert = "Buen trabajo";
                image = ResourcesPackages.getRandomSuccesImage();
                break;
            case ERROR:
                titleAlert = "Ooops";
                image = ResourcesPackages.getRandomErrorImage();
                break;
        }
    }
}
