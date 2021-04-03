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

import com.jfoenix.controls.JFXDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import com.laynezcoder.estfx.database.DatabaseHelper;
import javafx.scene.input.KeyCode;

public class JFXDialogTool extends JFXDialog {

    public JFXDialogTool(Region region, StackPane container) {
        setContent(region);
        region.setVisible(true);
        setBackground(Background.EMPTY);
        setDialogContainer(container);
        getStyleClass().add("jfx-dialog-overlay-pane");
        setTransitionType(DatabaseHelper.dialogTransition());

        container.requestFocus();
        container.setOnKeyReleased(ev -> {
            if(!isVisible()) return;
            
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                close();
            }
        });
    }
}
