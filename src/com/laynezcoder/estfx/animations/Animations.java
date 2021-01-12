package com.laynezcoder.estfx.animations;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOut;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXProgressBar;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animations {

    public static void fadeInUp(Node node) {
        new FadeInUp(node).play();
    }

    public static void fadeOut(Node node) {
        new FadeOut(node).play();
    }

    public static void shake(Node node) {
        new Shake(node).play();
    }

    public static void fadeOutWithDuration(Node node) {
        FadeOut fadeOut = new FadeOut(node);
        fadeOut.setSpeed(10);
        fadeOut.play();
    }

    public static void tooltip(Node node, Node tooltip) {
        node.setOnMouseEntered(ev -> {
            FadeIn fadeIn = new FadeIn(tooltip);
            fadeIn.setSpeed(3);
            fadeIn.play();
            tooltip.setVisible(true);
        });

        node.setOnMouseExited(ev -> {
            tooltip.setVisible(false);
        });
    }

    public static void hover(Node node, int duration, double setXAndY) {
        ScaleTransition scaleTrans = new ScaleTransition(Duration.millis(duration), node);
        scaleTrans.setFromX(1.0);
        scaleTrans.setFromY(1.0);
        scaleTrans.setToX(setXAndY);
        scaleTrans.setToY(setXAndY);

        node.setOnMouseEntered(ev -> {
            scaleTrans.setRate(1.0);
            scaleTrans.play();
        });

        node.setOnMouseExited(ev -> {
            scaleTrans.setRate(-1.0);
            scaleTrans.play();
        });
    }

    public static void progressAnimation(JFXProgressBar progressBar, double value) {
        Timeline timeline = new Timeline();

        KeyValue keyValue = new KeyValue(progressBar.progressProperty(), value);
        KeyFrame keyFrame = new KeyFrame(new Duration(600), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
    }
}
