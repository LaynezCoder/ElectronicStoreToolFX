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

import com.laynezcoder.estfx.constants.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExpandTextArea {

    private TextArea textArea;

    private TextArea expandText;

    private Stage stage;

    private Button button;

    public ExpandTextArea(Stage owner, TextArea textArea) {
        this.textArea = textArea;
        createWorkSpace(owner);
    }

    private void createWorkSpace(Stage owner) {
        expandText = new TextArea();
        VBox.setVgrow(expandText, Priority.ALWAYS);

        button = new Button("Set text");
        button.setPrefSize(150, 25);
        VBox.setVgrow(button, Priority.ALWAYS);

        VBox root = new VBox(expandText, button);
        root.setSpacing(5);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Constants.LIGHT_THEME);
        button.getStyleClass().add("btn-delete");
        expandText.getStyleClass().addAll("text-area", "expand-text-area");

        stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(owner);
        stage.setTitle("Description");
        stage.getIcons().add(Constants.ICON);
        stage.initModality(Modality.APPLICATION_MODAL);

        button.setOnAction(ev -> {
            String writtenText = expandText.getText();
            textArea.setText(writtenText);
            stage.hide();
        });

        root.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                stage.hide();
            }
        });
    }

    public void show() {
        if (!textArea.isEditable()) {
            button.setDisable(true);
            expandText.setEditable(false);
        }
        
        String textAreaText = textArea.getText();
        expandText.setText(textAreaText);
        stage.show();
    }
}
