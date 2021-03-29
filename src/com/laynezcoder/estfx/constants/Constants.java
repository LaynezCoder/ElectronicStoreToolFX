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
package com.laynezcoder.estfx.constants;

import javafx.scene.effect.BoxBlur;

public final class Constants {
    
    private Constants() {
        
    }

    public static final String TITLE = "Electronic Store Tool FX";
    public static final Double MIN_WIDTH = 1040.00;
    public static final Double MIN_HEIGHT = 640.00;

    public static final String SOURCE_PACKAGE = "/com/laynezcoder/estfx";
    public static final String IMAGE_PACKAGE = "/resources/com/laynezcoder/images/";
    public static final String FXML_PACKAGE = SOURCE_PACKAGE + "/views/";
    public static final String PROFILE_PICTURES_PACKAGE = IMAGE_PACKAGE + "profiles/";

    public static final String LOGIN_VIEW = FXML_PACKAGE + "LoginView.fxml";
    public static final String START_VIEW = FXML_PACKAGE + "StartView.fxml";
    public static final String MAIN_VIEW = FXML_PACKAGE + "MainView.fxml";

    public static final String STAGE_ICON = IMAGE_PACKAGE + "icon.png";
    public static final String NO_IMAGE_AVAILABLE = IMAGE_PACKAGE + "empty-image.jpg";
    public static final String INFORMATION_IMAGE = IMAGE_PACKAGE + "information.png";
    public static final String ERROR_IMAGE = IMAGE_PACKAGE + "error.png";
    public static final String SUCCESS_IMAGE = IMAGE_PACKAGE + "success.png";

    public static final String CSS_LIGHT_THEME = "/resources/com/laynezcoder/light-theme.css";
    public static final String LIGHT_THEME = Constants.class.getResource(CSS_LIGHT_THEME).toExternalForm();
   
    public static final String EXISTENT = "Existent";
    public static final String NOT_EXISTENT = "Not existent";

    public static final String REALIZED = "Realized";
    public static final String NOT_REALIZED = "Not realized";

    public static final String REPORTED = "Reported";
    public static final String NOT_REPORTED = "Not reported";

    public static final BoxBlur BOX_BLUR_EFFECT = new BoxBlur(3, 3, 3);
}
