package com.laynezcoder.estfx.resources;

import javafx.scene.effect.BoxBlur;

public class Constants {

    public static final String TITLE = "Electronic Store Tool FX";

    public static final String SOURCE_PACKAGES = "/com/laynezcoder/estfx";
    public static final String MEDIA_PACKAGE = SOURCE_PACKAGES + "/media/";
    public static final String VIEWS_PACKAGE = SOURCE_PACKAGES + "/views/";
    public static final String PROFILE_PICTURES_PACKAGE = MEDIA_PACKAGE + "profile/";
    public static final String RESOURCES_PACKAGE = SOURCE_PACKAGES + "/resources/";
    public static final String FONTS_PACKAGE = SOURCE_PACKAGES + "/fonts/";

    public static final String LOGIN_VIEW = VIEWS_PACKAGE + "LoginView.fxml";
    public static final String START_VIEW = VIEWS_PACKAGE + "StartView.fxml";

    public static final String NO_IMAGE_AVAILABLE = MEDIA_PACKAGE + "empty-image.jpg";
    public static final String ICON = MEDIA_PACKAGE + "icon.png";

    public static final String CSS_LIGHT_THEME = RESOURCES_PACKAGE + "LightTheme.css";
    public static final String LIGHT_THEME = Constants.class.getResource(CSS_LIGHT_THEME).toExternalForm();

    public static final String FONT = FONTS_PACKAGE + "rimouski.ttf";
    public static final String RIMOUSKI_FONT = Constants.class.getResource(FONT).toExternalForm();

    public static final String ERROR_CONNECTON_MYSQL = "An error has occurred, please check the connection to mySQL.";

    public static final BoxBlur BOX_BLUR_EFFECT = new BoxBlur(3, 3, 3);
}
