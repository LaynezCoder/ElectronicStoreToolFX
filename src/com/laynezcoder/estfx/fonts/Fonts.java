package com.laynezcoder.estfx.fonts;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Fonts {

    public static final String RIMOUSKI_FONT = "/com/laynezcoder/estfx/fonts/rimouski.ttf";

    public static void toText(Text text, int sizeFont) {
        Font font = Font.loadFont(Fonts.class.getResourceAsStream(RIMOUSKI_FONT), sizeFont);
        text.setFont(font);
    }

    public static void toButton(Button btn, int sizeFont) {
        Font font = Font.loadFont(Fonts.class.getResourceAsStream(RIMOUSKI_FONT), sizeFont);
        btn.setFont(font);
    }
}
