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
package com.laynezcoder.estfx.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javax.imageio.ImageIO;

public class CropImageProfile {

    private final String EXTENSION = "jpg";

    private ImageView imageView;

    private double imageHeight;

    private double imageWidth;

    private final File file;

    private InputStream outputStream;

    private InputStream inputStream;

    private Rectangle rect;

    private Group group;

    public CropImageProfile(File file) {
        this.file = file;
        createWorkSpace();
    }

    private void createWorkSpace() {
        group = new Group();

        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CropImageProfile.class.getName()).log(Level.SEVERE, null, ex);
        }

        Image image = new Image(inputStream);
        imageHeight = image.getHeight();
        imageWidth = image.getWidth();

        imageView = new ImageView(image);
        imageView.setFitHeight(imageHeight);
        imageView.setFitWidth(imageWidth);
        imageView.setPreserveRatio(true);

        rect = new Rectangle();
        group.getChildren().addAll(imageView, rect);

        if (imageWidth > imageHeight) {
            rect.setWidth(imageHeight);
            rect.setHeight(imageHeight);
            rect.setX(imageWidth / 2 - imageHeight / 2);
            rect.setY(0);
        } else if (imageHeight > imageWidth) {
            rect.setWidth(imageWidth);
            rect.setHeight(imageWidth);
            rect.setX(0);
            rect.setY(imageHeight / 2 - imageWidth / 2);
        } else if (imageHeight == imageWidth) {
            group.getChildren().remove(rect);
        }
    }

    public InputStream getInputStream() throws FileNotFoundException {
        if (!group.getChildren().contains(rect)) {
            return new FileInputStream(file);
        } else {
            Bounds bounds = rect.getBoundsInParent();
            int width = (int) bounds.getWidth();
            int height = (int) bounds.getHeight();

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            parameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), width, height));

            WritableImage wi = new WritableImage(width, height);
            imageView.snapshot(parameters, wi);

            BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
            BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

            Graphics2D graphics = bufImageRGB.createGraphics();
            graphics.drawImage(bufImageARGB, 0, 0, null);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                ImageIO.write(bufImageRGB, EXTENSION, os);
            } catch (IOException ex) {
                Logger.getLogger(CropImageProfile.class.getName()).log(Level.SEVERE, null, ex);
            }
            outputStream = new ByteArrayInputStream(os.toByteArray());
            graphics.dispose();
        }
        return outputStream;
    }
}
