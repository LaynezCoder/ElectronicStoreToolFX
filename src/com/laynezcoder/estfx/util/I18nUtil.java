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

import com.laynezcoder.estfx.constants.ResourcesPackages;
import com.laynezcoder.estfx.constants.Views;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class I18NUtil {

    private static final String BUNDLE = "resources.com.laynezcoder.i18n.gui";
   
    public static Parent loadView(Views nameView) throws IOException {
        Locale.setDefault(Locale.getDefault());

        Parent root = FXMLLoader.load(
                I18NUtil.class.getResource(ResourcesPackages.FXML_PACKAGE + nameView.getValueWithExtension()),
                ResourceBundle.getBundle(BUNDLE));

        return root;
    }
}
