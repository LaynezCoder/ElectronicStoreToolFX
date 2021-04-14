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

import javafx.scene.image.Image;

public final class ResourcesPackages {
    
    private final static int NUMBER_OF_ERROR_IMAGES = 18;
    private final static int NUMBER_OF_SUCCES_IMAGES = 32;

    private ResourcesPackages() {}

    private static final String RESOURCES = "/resources/com/laynezcoder";
    public static final String IMAGES_PACKAGE = RESOURCES + "/images/";
    public static final String UI_IMAGES_PACKAGE = IMAGES_PACKAGE + "ui/";
    public static final String UI_ALERTS_IMAGES_PACKAGE = UI_IMAGES_PACKAGE + "alerts/";
    public static final String FLATICON_IMAGES_PACKAGE = IMAGES_PACKAGE + "flaticon/";
    public static final String KITTY_IMAGES_PACKAGE = FLATICON_IMAGES_PACKAGE + "kitty/";
    private static final String DINOSAUR_IMAGES_PACKAGE = FLATICON_IMAGES_PACKAGE + "dinosaur/";
    public static final String DINOSAUR_ERROR_IMAGES_PACKAGE = DINOSAUR_IMAGES_PACKAGE + "error/";
    public static final String DINOSAUR_SUCCES_IMAGES_PACKAGE = DINOSAUR_IMAGES_PACKAGE + "succes/";
    public static final String PEOPLE_IMAGES_PACKAGE = FLATICON_IMAGES_PACKAGE + "people/";
    public static final String PROFILE_PICTURES_PACKAGE = IMAGES_PACKAGE + "profiles/";
    public static final String FXML_PACKAGE = RESOURCES + "/fxml/";

    public static final Image DELETE_IMAGE = new Image(DINOSAUR_ERROR_IMAGES_PACKAGE + "12.png", 100, 100, true, true);

    public static final String NO_IMAGE_AVAILABLE = UI_IMAGES_PACKAGE + "empty-image.jpg";
    public static final String INFORMATION_ICON = UI_ALERTS_IMAGES_PACKAGE + "information.png";
    public static final String ERROR_ICON = UI_ALERTS_IMAGES_PACKAGE + "error.png";
    public static final String SUCCESS_ICON = UI_ALERTS_IMAGES_PACKAGE + "success.png";

    public static Image getRandomErrorImage() {
        String path = DINOSAUR_ERROR_IMAGES_PACKAGE + randomNumber(NUMBER_OF_ERROR_IMAGES);
        return new Image(path, 110, 170, true, true);
    }

    public static Image getRandomSuccesImage() {
        String path = DINOSAUR_SUCCES_IMAGES_PACKAGE + randomNumber(NUMBER_OF_SUCCES_IMAGES);
        return new Image(path, 110, 170, true, true);
    }

    private static String randomNumber(int size) {
        int index = (int) (Math.random() * size);
        return index + ".png";
    }
}
