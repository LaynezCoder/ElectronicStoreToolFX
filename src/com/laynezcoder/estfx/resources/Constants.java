package com.laynezcoder.estfx.resources;

public class Constants {

    public static final String MEDIA_PACKAGE = "/com/laynezcoder/estfx/media/";
    public static final String SOURCE_PACKAGES = "/com/laynezcoder/estfx";
    public static final String CSS_LIGHT_THEME = "/com/laynezcoder/estfx/resources/LightTheme.css";
    public static final String NO_IMAGE_AVAILABLE = MEDIA_PACKAGE + "empty-image.jpg";
    public static final String PROFILE_PICTURES_PACKAGE = MEDIA_PACKAGE + "profile/";
    public static final String ICON = MEDIA_PACKAGE + "icon.png";
    public static final String LIGHT_THEME = Constants.class.getResource(CSS_LIGHT_THEME).toExternalForm();
}
