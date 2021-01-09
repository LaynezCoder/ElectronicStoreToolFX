package com.laynezcoder.estfx;

import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.resources.Resources;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
            DatabaseHelper.logout();
            Parent root = FXMLLoader.load(getClass().getResource("/com/laynezcoder/views/LoginView.fxml"));
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setTitle("Electronic Store Tool FX");
            stage.getIcons().add(new Image(Resources.SOURCE_PACKAGES + "/media/reicon.png"));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startWindow(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/laynezcoder/views/StartView.fxml"));
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.setTitle("Electronic Store Tool FX");
            stage.getIcons().add(new Image(Resources.SOURCE_PACKAGES + "/media/reicon.png"));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
