package com.laynezcoder.estfx.util;

import com.laynezcoder.estfx.models.UserSession;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.time.LocalTime;
import javafx.scene.text.Text;

public class Weather {

    private static final UserSession SESSION = UserSession.getInstace();
    private static final LocalTime EARLY_MORNING = LocalTime.of(0, 0, 0);
    private static final LocalTime MORNING = LocalTime.of(06, 0, 0);
    private static final LocalTime AFTERNOON = LocalTime.of(12, 0, 0);
    private static final LocalTime NIGHT = LocalTime.of(18, 0, 0);
    private static final LocalTime NOW = LocalTime.now();

    public static void compare(MaterialDesignIconView icon, Text text) {
        System.out.println(SESSION.getName());
        if (between(EARLY_MORNING, MORNING)) {
            text.setText("Good Morning, " + SESSION.getName());
            icon.setIcon(MaterialDesignIcon.WEATHER_WINDY_VARIANT);
        } else if (between(MORNING, AFTERNOON)) {
            text.setText("Good morning, " + SESSION.getName());
            icon.setIcon(MaterialDesignIcon.WEATHER_SUNSET);
        } else if (between(AFTERNOON, NIGHT)) {
            text.setText("Good afternoon, " + SESSION.getName());
            icon.setIcon(MaterialDesignIcon.WEATHER_PARTLYCLOUDY);
        } else {
            text.setText("Good night, " + SESSION.getName());
            icon.setIcon(MaterialDesignIcon.WEATHER_NIGHT);
        }
    }

    private static boolean between(LocalTime start, LocalTime end) {
        return NOW.isAfter(start) && NOW.isBefore(end);
    }
}
