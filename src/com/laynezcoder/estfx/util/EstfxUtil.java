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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class EstfxUtil {

    private static final char SPACE = ' ';

    public static String getNameWithoutSpaces(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == SPACE) {
                return name.substring(0, i);
            }
        }
        return name;
    }
    
    public static String trimText(String value, int trimSize) {
        if(value.length() > trimSize) {
            return value.substring(0, trimSize - 1) + "...";
        }
        return value;
    }

    public static void openBrowser(String url, Node node) {
        node.setOnMouseClicked(ev -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(EstfxUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public static void disableButton(ActionEvent event, Button button) {
        if (event.getSource().equals(button)) {
            button.setDisable(true);
        } else {
            button.setDisable(false);
        }
    }
}
