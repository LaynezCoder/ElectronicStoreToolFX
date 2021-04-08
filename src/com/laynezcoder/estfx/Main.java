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
package com.laynezcoder.estfx;

import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.constants.Constants;
import com.laynezcoder.estfx.constants.Views;
import com.laynezcoder.estfx.util.I18NUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        if (DatabaseHelper.checkIfUserExists() == 0) {
            startWindow(stage);
        } else {
            loginWindow(stage);
        }
    }

    private void loginWindow(Stage stage) {
        try {
            Parent root = I18NUtil.loadView(Views.LOGIN);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            stage.getIcons().add(Constants.ICON);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setTitle(Constants.TITLE);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startWindow(Stage stage) {
        try {
            Parent root = I18NUtil.loadView(Views.START);
            stage.getIcons().add(Constants.ICON);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.setTitle(Constants.TITLE);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
