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
package com.laynezcoder.estfx.mask;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ValidatorsBuilder {
    
    public static void toTextField(TextField txt) {
        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                
            }
        });
    }

    public static void toPasswordField(PasswordField txt) {
        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                
            }
        });
    }

    public static void toTextArea(TextArea txt) {
        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                
            }
        });
    }

    public static void toComboBox(ComboBox comboBox) {
        comboBox.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                
            }
        });
    }

    public static void toDatePicker(DatePicker datePicker) {
        datePicker.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                
            }
        });
    }
}
