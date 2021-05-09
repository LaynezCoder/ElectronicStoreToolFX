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
package com.laynezcoder.estfx.ui;

import com.laynezcoder.estfx.ui.JFXDialogTool;
import com.laynezcoder.estfx.models.UserSession;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ConfirmationForm extends JFXDialogTool {

    private static final UserSession SESSION = UserSession.getInstace();

    private static Button btn;

    private static TextField txt;

    private static String theWritted;

    public ConfirmationForm(StackPane container, String record) {
        super(getContent(record), container);
    }

    private static Region getContent(String record) {
        Text title = new Text("¿Está absolutamente seguro?");
        setStyle(title, "h3");

        Text subtitle = new Text("¡Sucederán cosas malas inesperadas si no lees esto!");
        setStyle(subtitle, "sub-title");

        Text contentOne = new Text("Esta acción no se puede deshacer. Esto eliminará permanentemente el registro");
        setStyle(contentOne, "description");

        Text user = new Text(record);
        setStyle(user, "h4");

        Text contentTwo = new Text(", además de eliminar todos los clientes, cotizaciones o productos relacionados.");
        setStyle(contentTwo, "description");

        TextFlow textContainer = new TextFlow();
        textContainer.getChildren().addAll(contentOne, new Text(" "), user, new Text(" "), contentTwo);

        Text contentThree = new Text("Porfavor, escriba");
        setStyle(contentThree, "description");

        theWritted = SESSION.getName() + "/" + record;
        Text textToWrite = new Text(theWritted);
        setStyle(textToWrite, "h4");

        Text contentFour = new Text("para confirmar.");
        setStyle(contentFour, "sub-title");

        TextFlow textToWriteContainer = new TextFlow();
        textToWriteContainer.getChildren().addAll(contentThree, new Text(" "), textToWrite, new Text(" "), contentFour);

        txt = new TextField();
        setStyle(txt, "txt-confirm-delete");

        btn = new Button("Entiendo las consecuencias, deseo eliminar este registro");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setMinHeight(40);
        setStyle(btn, "btn-add");

        VBox root = new VBox();
        root.setSpacing(10);
        root.setPrefSize(430, 300);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("card");
        root.getChildren().addAll(title, subtitle, textContainer, textToWriteContainer, txt, btn);
        return root;
    }

    private static void setStyle(Node node, String style) {
        node.getStyleClass().add(style);
    }

    public void setOnAction(EventHandler<ActionEvent> event) {
        btn.setDisable(true);
        txt.setOnKeyReleased(ev -> {
            if (txt.getText().equals(theWritted)) {
                btn.setDisable(false);
            } else {
                btn.setDisable(true);
            }
        });

        btn.setOnAction(event);
    }
}
