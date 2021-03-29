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

import com.laynezcoder.estfx.constants.Views;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class I18nUtil {

    private final String EN = "";
    
    private final String ES = "";
    
    private Parent loadView(Views nameView) {
        Parent root = null;
        try {
            root = FXMLLoader.load(I18nUtil.class.getResource(nameView.getValueWithExtension()), ResourceBundle.getBundle("i18n/mensajes", Locale.getDefault()));
        } catch (IOException ex) {
            Logger.getLogger(I18nUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return root;
    }
}
