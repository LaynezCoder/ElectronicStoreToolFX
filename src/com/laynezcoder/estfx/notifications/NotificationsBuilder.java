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
package com.laynezcoder.estfx.notifications;

import com.laynezcoder.estfx.resources.Constants;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class NotificationsBuilder {

    private static Image icon;

    private static String title;

    public static void create(NotificationType type, String message) {
        setFunction(type);
        Notifications notifications = Notifications.create();
        notifications.title(title);
        notifications.text(message);
        notifications.graphic(new ImageView(icon));
        notifications.hideAfter(Duration.seconds(6));
        notifications.position(Pos.BASELINE_RIGHT);
        notifications.show();
    }

    private static void setFunction(NotificationType type) {
        switch (type) {
            case INFORMATION:
                title = "¡Information!";
                icon = new Image(Constants.INFORMATION_IMAGE);
            break;
            
            case ERROR:
                title = "¡Error!";
                icon = new Image(Constants.ERROR_IMAGE);
            break;
            
            case SUCCESS:
                title = "¡Success!";
                icon = new Image(Constants.SUCCESS_IMAGE);
            break;
            
            case INVALID_ACTION:
                title = "¡Invalid action!";
                icon = new Image(Constants.ERROR_IMAGE);
            break;
        }
    }
}
