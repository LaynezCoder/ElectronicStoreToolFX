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
package com.laynezcoder.estfx.controllers;

import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.constants.Constants;
import com.laynezcoder.estfx.constants.Messages;
import com.laynezcoder.estfx.constants.ResourcesPackages;
import com.laynezcoder.estfx.constants.UserType;
import com.laynezcoder.estfx.constants.Views;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.models.UserSession;
import com.laynezcoder.estfx.util.I18NUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable {

    private final UserSession SESSION = UserSession.getInstace();
    
    private final String IMAGE = ResourcesPackages.UI_IMAGES_PACKAGE + "login.png";

    private final String INCORRECT_CREDENTIALS = "Incorrect user or password";

    @FXML
    private ImageView image;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtPassword;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private FontAwesomeIconView icon;

    @FXML
    private Text title;

    private double x, y;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showPassword();
        selectText();
        setMask();
        animations();
        loadImage();
    }

    private void loadImage() {
        image.setImage(new Image(IMAGE, 350, 290, true, true));
    }

    private void animations() {
        Animations.fadeInUp(title);
        Animations.fadeInUp(txtUser);
        Animations.fadeInUp(txtPassword);
        Animations.fadeInUp(pfPassword);
        Animations.fadeInUp(btnLogin);
    }

    private void setMask() {
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtUser, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtPassword, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(pfPassword, 40);
    }

    private void selectText() {
        TextFieldMask.selectText(txtUser);
        TextFieldMask.selectText(txtPassword);
        TextFieldMask.selectText(pfPassword);
    }

    @FXML
    private void login() {
        String user = txtUser.getText().trim();
        String pass = pfPassword.getText().trim();

        if (user.isEmpty() && pass.isEmpty()) {
            Animations.shake(txtUser);
            Animations.shake(pfPassword);
            Animations.shake(icon);
            NotificationsBuilder.create(NotificationType.ERROR, Messages.INSUFFICIENT_DATA);
            return;
        }

        if (user.isEmpty()) {
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        if (pass.isEmpty()) {
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            Animations.shake(icon);
            return;
        }

        try {
            String sql = "SELECT id, fullname, username, pass, biography, userType, linkProfile FROM Users WHERE username = BINARY ? AND pass = BINARY ?";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, pass);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                setSession(rs);
                loadMain();
                DatabaseHelper.insertUserSession(SESSION.getId());
                NotificationsBuilder.create(NotificationType.SUCCESS, "Welcome to the system " + SESSION.getName() + "!");
            } else {
                NotificationsBuilder.create(NotificationType.ERROR, INCORRECT_CREDENTIALS);
                Animations.shake(txtUser);
                Animations.shake(pfPassword);
                Animations.shake(txtPassword);
                Animations.shake(icon);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }
    }

    private void setSession(ResultSet rs) throws SQLException {
        SESSION.setId(rs.getInt(1));
        SESSION.setName(rs.getString(2));
        SESSION.setUsername(rs.getString(3));
        SESSION.setPassword(rs.getString(4));
        SESSION.setBiography(rs.getString(5));
        SESSION.setUserType(rs.getString(6));
        SESSION.setLinkProfile(rs.getString(7));
    }

    private void loadMain() {
        try {
            FXMLLoader loader = I18NUtil.FXMLLoader(Views.MAIN);   
            Parent root = loader.load();
            MainController main = loader.getController();

            if (!SESSION.getUserType().equals(UserType.ADMINSTRATOR.value())) {
                main.removeButtons();
            }
             
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.getIcons().add(Constants.ICON);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setMinHeight(Constants.MIN_HEIGHT);
            stage.setMinWidth(Constants.MIN_WIDTH);
            stage.setTitle(Constants.TITLE);
            stage.setScene(new Scene(root));
            stage.show();
            closeStage();

            root.setOnKeyPressed((KeyEvent e) -> {
                if (e.getCode().equals(KeyCode.F11)) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showPassword() {
        txtPassword.managedProperty().bind(icon.pressedProperty());
        txtPassword.visibleProperty().bind(icon.pressedProperty());
        txtPassword.textProperty().bindBidirectional(pfPassword.textProperty());

        pfPassword.managedProperty().bind(icon.pressedProperty().not());
        pfPassword.visibleProperty().bind(icon.pressedProperty().not());

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
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }
}
