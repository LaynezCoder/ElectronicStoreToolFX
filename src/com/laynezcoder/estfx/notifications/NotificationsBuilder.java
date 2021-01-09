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
                icon = new Image(Constants.MEDIA_PACKAGE + "information.png");
                break;
            case ERROR:
                title = "¡Error!";
                icon = new Image(Constants.MEDIA_PACKAGE + "error.png");
                break;
            case SUCCESS:
                title = "¡Success!";
                icon = new Image(Constants.MEDIA_PACKAGE + "success.png");
                break;
            case INVALID_ACTION:
                title = "¡Invalid action!";
                icon = new Image(Constants.MEDIA_PACKAGE + "error.png");
                break;
        }
    }
}
