package com.laynezcoder.estfx.fonts;

import com.laynezcoder.estfx.resources.Constants;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Fonts {

    public static void toText(Text text, int sizeFont) {
        Font font = Font.loadFont(Constants.RIMOUSKI_FONT, sizeFont);
        text.setFont(font);
    }

    public static void toButton(Button btn, int sizeFont) {
        Font font = Font.loadFont(Constants.RIMOUSKI_FONT, sizeFont);
        btn.setFont(font);
    }
}
