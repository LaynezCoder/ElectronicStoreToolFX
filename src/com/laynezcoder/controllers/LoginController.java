package com.laynezcoder.controllers;

import animatefx.animation.FadeIn;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.database.DatabaseConnection;
import com.laynezcoder.database.DatabaseHelper;
import com.laynezcoder.resources.Resources;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable {

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXPasswordField pfPassword;

    @FXML
    private FontAwesomeIconView icon;

    @FXML
    private Text title;

    @FXML
    private MaterialDesignIconView iconWeb;

    @FXML
    private MaterialDesignIconView iconFacebook;

    @FXML
    private MaterialDesignIconView iconInstagram;

    private double x, y;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showPassword();
        validationJFXTextField();
        selectText();
        setFonts();
        animations();
        setURL();
        maxinumCharactert();
    }

    private void setURL() {
        Resources.url("", iconWeb);
        Resources.url("", iconFacebook);
        Resources.url("", iconInstagram);
    }

    private void animations() {
        Resources.hoverAnimation(iconWeb, 50, 1.2);
        Resources.hoverAnimation(iconFacebook, 50, 1.2);
        Resources.hoverAnimation(iconInstagram, 50, 1.2);

        FadeAnimation(title);
        FadeAnimation(txtUser);
        FadeAnimation(txtPassword);
        FadeAnimation(pfPassword);
        FadeAnimation(btnLogin);
        FadeAnimation(iconWeb);
        FadeAnimation(iconFacebook);
        FadeAnimation(iconInstagram);
    }

    private void setFonts() {
        Resources.setFontToText(title, 25);
        Resources.setFontToJFXButton(btnLogin, 15);
    }

    private void validationJFXTextField() {
        Resources.validationOfJFXTextField(txtUser);
        Resources.validationOfJFXTextField(txtPassword);
        Resources.validationOfJFXPasswordField(pfPassword);
    }

    private void selectText() {
        Resources.selectTextToJFXTextField(txtUser);
        Resources.selectTextToJFXTextField(txtPassword);
        Resources.selectTextToJFXPasswordField(pfPassword);
    }

    @FXML
    private void login() {
        String email = txtUser.getText();
        String pass = pfPassword.getText();

        if (email.isEmpty() && pass.isEmpty()) {
            Resources.notification("Error", "Insufficient data!", "error.png");
            shakeAnimation(txtUser);
            shakeAnimation(pfPassword);
        } else if (email.isEmpty()) {
            Resources.notification("Error", "Insufficient data!", "error.png");
            shakeAnimation(txtUser);
        } else if (pass.isEmpty()) {
            Resources.notification("Error", "Insufficient data!", "error.png");
            shakeAnimation(pfPassword);
        } else {
            try {
                String sql = "SELECT id, nameUser FROM Users WHERE email = BINARY ? AND pass = BINARY ?";
                PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, pass);

                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nameUser = rs.getString("nameUser");

                    boolean result = DatabaseHelper.insertUsserSession(id);
                    if (result) {
                        loadMain();
                        Resources.notification("Success", "Welcome to the system " + nameUser + "!", "check.png");
                    } else {
                        Resources.notification("FATAL ERROR", "An error occurred when connecting to MySQL.", "error.png");
                    }
                } else {
                    Resources.notification("Error", "Incorrect user or password!", "error.png");
                    shakeAnimation(txtUser);
                    shakeAnimation(pfPassword);
                    shakeAnimation(txtPassword);
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                Resources.notification("FATAL ERROR", "An error occurred when connecting to MySQL.", "error.png");
            }
        }
    }

    private void loadMain() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(Resources.SOURCE_PACKAGES + "/views/MainView.fxml"));
            Parent root = loader.load();
            MainController main = loader.getController();

            if (DatabaseHelper.getUserType().equals("Administrator")) {
                main.getBtnStatistics().setVisible(true);
                main.getBtnAddUser().setVisible(true);
                main.getBtnAbout().setVisible(true);
            } else {
                main.getBtnStatistics().setVisible(false);
                main.getBtnAddUser().setVisible(false);
                main.getBtnAbout().setVisible(false);
            }

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.getIcons().add(new Image(Resources.SOURCE_PACKAGES + "/media/reicon.png"));
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setTitle("Electronic Store Tool FX");
            stage.setScene(new Scene(root));
            stage.show();
            closeStage();

            root.setOnKeyPressed((KeyEvent e) -> {
                if (e.getCode() == KeyCode.F11) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            });

            stage.setOnCloseRequest(ev -> {
                DatabaseHelper.logout();
            });
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void showPassword() {
        txtPassword.managedProperty().bind(icon.pressedProperty());
        txtPassword.visibleProperty().bind(icon.pressedProperty());

        pfPassword.managedProperty().bind(icon.pressedProperty().not());
        pfPassword.visibleProperty().bind(icon.pressedProperty().not());

        txtPassword.textProperty().bindBidirectional(pfPassword.textProperty());

        icon.pressedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal) {
                icon.setIcon(FontAwesomeIcon.EYE);
            } else {
                icon.setIcon(FontAwesomeIcon.EYE_SLASH);
            }
        });
    }

    @FXML
    private void closeStage() {
        ((Stage) txtUser.getScene().getWindow()).close();
    }

    @FXML
    private void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    private void dragged(MouseEvent event) {
        Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stg.setX(event.getScreenX() - x);
        stg.setY(event.getScreenY() - y);
    }

    private void shakeAnimation(Node node) {
        new Shake(node).play();
    }

    private void FadeAnimation(Node node) {
        new FadeIn(node).play();
    }

    private void maxinumCharactert() {
        Resources.limitTextField(txtUser, 150);
        Resources.limitTextField(txtPassword, 150);
        Resources.limitTextField(pfPassword, 150);
    }
}
