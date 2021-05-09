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
import java.util.regex.Pattern;
import javafx.event.Event;
import javafx.scene.Node;
import org.apache.commons.validator.UrlValidator;

public class EstfxUtil {

    public static String trimText(String value, int size) {
        if (value.length() > size) {
            return value.substring(0, size - 1) + "...";
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

    public static void disableNode(Event event, Node node) {
        if (event.getSource().equals(node)) {
            node.setDisable(true);
        } else {
            node.setDisable(false);
        }
    }

    public static boolean validateURL(String URL) {
        UrlValidator validator = new UrlValidator();
        return validator.isValid(URL);
    }

    public static boolean validateEmailAddress(String email) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }
}
