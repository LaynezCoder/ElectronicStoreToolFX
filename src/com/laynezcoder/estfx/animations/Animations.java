package com.laynezcoder.estfx.animations;

import animatefx.animation.FadeInUp;
import animatefx.animation.Shake;
import javafx.scene.Node;

public class Animations {

    public static void fadeInUp(Node node) {
        new FadeInUp(node).play();
    }

    public static void shake(Node node) {
        new Shake(node).play();
    }
}
