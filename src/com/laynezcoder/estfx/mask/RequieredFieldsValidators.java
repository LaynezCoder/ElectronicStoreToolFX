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

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class RequieredFieldsValidators {

    private static final FontAwesomeIcon WARNING_ICON = FontAwesomeIcon.EXCLAMATION_TRIANGLE;
    
    private static final String MESSAGE = "Obligatory field";

    public static void toJFXTextField(JFXTextField txt) {
        RequiredFieldValidator validator = new RequiredFieldValidator(MESSAGE);
        validator.setIcon(new FontAwesomeIconView(WARNING_ICON));

        txt.getValidators().add(validator);
        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                txt.validate();
            }
        });
    }

    public static void toJFXPasswordField(JFXPasswordField txt) {
        RequiredFieldValidator validator = new RequiredFieldValidator(MESSAGE);
        validator.setIcon(new FontAwesomeIconView(WARNING_ICON));

        txt.getValidators().add(validator);
        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                txt.validate();
            }
        });
    }

    public static void toJFXTextArea(JFXTextArea txt) {
        RequiredFieldValidator validator = new RequiredFieldValidator(MESSAGE);
        validator.setIcon(new FontAwesomeIconView(WARNING_ICON));

        txt.getValidators().add(validator);
        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                txt.validate();
            }
        });
    }

    public static void toJFXComboBox(JFXComboBox comboBox) {
        RequiredFieldValidator validator = new RequiredFieldValidator(MESSAGE);
        validator.setIcon(new FontAwesomeIconView(WARNING_ICON));
        
        comboBox.getValidators().add(validator);
        comboBox.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                comboBox.validate();
            }
        });
    }

    public static void toJFXDatePicker(JFXDatePicker datePicker) {
        RequiredFieldValidator validator = new RequiredFieldValidator(MESSAGE);
        validator.setIcon(new FontAwesomeIconView(WARNING_ICON));
        
        datePicker.getValidators().add(validator);
        datePicker.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                datePicker.validate();
            }
        });
    }
}
