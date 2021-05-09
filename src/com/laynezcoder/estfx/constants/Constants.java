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
import javafx.scene.image.Image;

public final class Constants {
    
    private Constants() {}

    public static final String TITLE = "Electronic Store Tool FX";
    public static final Double MIN_WIDTH = 1040.00;
    public static final Double MIN_HEIGHT = 640.00;
    public static final Image ICON = new Image(ResourcesPackages.UI_IMAGES_PACKAGE + "icon.png");

    private static final String CSS_LIGHT_THEME = "/resources/com/laynezcoder/light-theme.css";
    public  static final String LIGHT_THEME = Constants.class.getResource(CSS_LIGHT_THEME).toExternalForm();
   
    public static final BoxBlur BLUR_EFFECT = new BoxBlur(3, 3, 3);
}
