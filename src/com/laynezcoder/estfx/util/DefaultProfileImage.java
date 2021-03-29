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

import com.laynezcoder.estfx.controllers.UsersController;
import com.laynezcoder.estfx.constants.ResourcesPackages;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DefaultProfileImage {

    private static final String ABC = "abcdefghijklmnopqrstvwxyz";

    public static InputStream getImage(String name) throws FileNotFoundException {
        InputStream inputStream = null;

        char character = name.toLowerCase().charAt(0);

        for (int i = 0; i < ABC.length(); i++) {
            if (ABC.charAt(i) == character) {
                inputStream = getProfilePictureFromPackage(String.valueOf(ABC.charAt(i)));
            }
        }
        return inputStream;
    }

    private static InputStream getProfilePictureFromPackage(String imageName) throws FileNotFoundException {
        return UsersController.class.getResourceAsStream(ResourcesPackages.PROFILE_PICTURES_PACKAGE + imageName + ".png");
    }
}
