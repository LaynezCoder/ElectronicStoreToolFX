package com.laynezcoder.estfx.util;

import com.laynezcoder.estfx.controllers.UsersController;
import com.laynezcoder.estfx.resources.Constants;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DefaultProfileImage {

    public static InputStream getImage(String name) throws FileNotFoundException {
        InputStream inputStream = null;

        char character = name.toLowerCase().charAt(0);
        switch (character) {
            case 'a':
                inputStream = getProfilePictureFromPackage("a");
                break;
            case 'b':
                inputStream = getProfilePictureFromPackage("b");
                break;
            case 'c':
                inputStream = getProfilePictureFromPackage("c");
                break;
            case 'd':
                inputStream = getProfilePictureFromPackage("d");
                break;
            case 'e':
                inputStream = getProfilePictureFromPackage("e");
                break;
            case 'f':
                inputStream = getProfilePictureFromPackage("f");
                break;
            case 'g':
                inputStream = getProfilePictureFromPackage("g");
                break;
            case 'h':
                inputStream = getProfilePictureFromPackage("h");
                break;
            case 'i':
                inputStream = getProfilePictureFromPackage("i");
                break;
            case 'j':
                inputStream = getProfilePictureFromPackage("j");
                break;
            case 'k':
                inputStream = getProfilePictureFromPackage("k");
                break;
            case 'l':
                inputStream = getProfilePictureFromPackage("l");
                break;
            case 'm':
                inputStream = getProfilePictureFromPackage("m");
                break;
            case 'n':
                inputStream = getProfilePictureFromPackage("n");
                break;
            case 'ñ':
                inputStream = getProfilePictureFromPackage("n");
                break;
            case 'o':
                inputStream = getProfilePictureFromPackage("o");
                break;
            case 'p':
                inputStream = getProfilePictureFromPackage("p");
                break;
            case 'q':
                inputStream = getProfilePictureFromPackage("q");
                break;
            case 'r':
                inputStream = getProfilePictureFromPackage("r");
                break;
            case 's':
                inputStream = getProfilePictureFromPackage("s");
                break;
            case 't':
                inputStream = getProfilePictureFromPackage("t");
                break;
            case 'u':
                inputStream = getProfilePictureFromPackage("u");
                break;
            case 'v':
                inputStream = getProfilePictureFromPackage("v");
                break;
            case 'w':
                inputStream = getProfilePictureFromPackage("w");
                break;
            case 'x':
                inputStream = getProfilePictureFromPackage("x");
                break;
            case 'y':
                inputStream = getProfilePictureFromPackage("y");
                break;
            case 'z':
                inputStream = getProfilePictureFromPackage("z");
                break;
        }
        return inputStream;
    }

    private static InputStream getProfilePictureFromPackage(String imageName) throws FileNotFoundException {
        return UsersController.class.getResourceAsStream(Constants.PROFILE_PICTURES_PACKAGE + imageName + ".png");
    }
}
