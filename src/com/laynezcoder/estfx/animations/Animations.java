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
package com.laynezcoder.estfx.animations;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOut;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXProgressBar;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
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

    public static void fade(Node parent, Node node, Node icon) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.0);

        node.setEffect(colorAdjust);

        parent.setOnMouseEntered(e -> {
            icon.setVisible(true);
            fadeInUp(icon);

            Timeline fadeInTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(colorAdjust.brightnessProperty(), colorAdjust.brightnessProperty().getValue(), Interpolator.LINEAR)),
                    new KeyFrame(Duration.seconds(0.2), new KeyValue(colorAdjust.brightnessProperty(), -0.6, Interpolator.LINEAR)
                    ));
            fadeInTimeline.setCycleCount(1);
            fadeInTimeline.setAutoReverse(false);
            fadeInTimeline.play();
        });

        parent.setOnMouseExited(e -> {
            fadeOut(icon);

            Timeline fadeOutTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(0),
                            new KeyValue(colorAdjust.brightnessProperty(), colorAdjust.brightnessProperty().getValue(), Interpolator.LINEAR)),
                    new KeyFrame(Duration.seconds(0.2), new KeyValue(colorAdjust.brightnessProperty(), 0, Interpolator.LINEAR)
                    ));
            fadeOutTimeline.setCycleCount(1);
            fadeOutTimeline.setAutoReverse(false);
            fadeOutTimeline.play();
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
