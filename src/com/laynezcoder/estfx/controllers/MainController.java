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

import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.constants.Constants;
import com.laynezcoder.estfx.constants.Views;
import com.laynezcoder.estfx.models.UserSession;
import com.laynezcoder.estfx.util.I18NUtil;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {

    @FXML
    private Button btnHome;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnQuotes;

    @FXML
    private Button btnProducts;

    @FXML
    private Button btnUsers;

    @FXML
    private Button btnStatistics;

    @FXML
    private Button btnExit;

    @FXML
    private AnchorPane container;

    @FXML
    private ImageView imageProfile;

    @FXML
    private VBox sideBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeWindowsInitialize();
        loadUserData();
    }

    private void loadUserData() {
        Image image = null;
        try {
            String sql = "SELECT profileImage FROM Users WHERE id = ?";
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ps.setInt(1, UserSession.getInstace().getId());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                InputStream img = rs.getBinaryStream("profileImage");
                if (img != null) {
                    image = new Image(img, 40, 40, true, true);
                }
            }

            showImage(image);
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showImage(Image image) {
        imageProfile.setImage(image);

        Circle clip = new Circle(17.5);
        clip.setCenterX(imageProfile.getFitWidth() / 2);
        clip.setCenterY(imageProfile.getFitHeight() / 2);
        imageProfile.setClip(clip);
    }

    private void setDisableButtons(ActionEvent event) {
        setDisableButton(event, btnHome);
        setDisableButton(event, btnCustomers);
        setDisableButton(event, btnQuotes);
        setDisableButton(event, btnProducts);
        setDisableButton(event, btnUsers);
        setDisableButton(event, btnStatistics);
        setDisableButton(event, btnExit);
    }

    private void homeWindowsInitialize() {
        btnHome.setDisable(true);
        showFXMLWindows(Views.HOME);
    }

    @FXML
    private void homeWindows(ActionEvent event) {
        showFXMLWindows(Views.HOME);
        setDisableButtons(event);
    }

    @FXML
    private void customersWindows(ActionEvent event) {
        showFXMLWindows(Views.CUSTOMERS);
        setDisableButtons(event);
    }

    @FXML
    private void quotesWindows(ActionEvent event) {
        showFXMLWindows(Views.QUOTES);
        setDisableButtons(event);
    }

    @FXML
    private void productsWindows(ActionEvent event) {
    }

    @FXML
    private void usersWindows(ActionEvent event) {
    }

    @FXML
    private void statisticsWindows(ActionEvent event) {
    }

    @FXML
    private void aboutWindows(ActionEvent event) {
    }

    @FXML
    private void settingsWindows(ActionEvent event) {
        setDisableButtons(event);
    }

    @FXML
    private void loginWindow() {
        try {
            Parent root = I18NUtil.loadView(Views.LOGIN);

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            Stage stage = new Stage(StageStyle.TRANSPARENT);
            stage.getIcons().add(Constants.ICON);
            stage.setScene(scene);
            stage.show();
            closeStage();
            UserSession.getInstace().logout();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeStage() {
        ((Stage) btnHome.getScene().getWindow()).close();
    }

    private void setDisableButton(ActionEvent event, Button button) {
        if (event.getSource().equals(button)) {
            button.setDisable(true);
        } else {
            button.setDisable(false);
        }
    }

    private void showFXMLWindows(Views FXMLName) {
        container.getChildren().clear();
        try {
            Parent root = I18NUtil.loadView(FXMLName);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            container.getChildren().setAll(root);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeButtons() {
        sideBar.getChildren().removeAll(btnUsers, btnStatistics);
    }

    public void addButtons() {
        if (sideBar.getChildren().contains(btnExit) || sideBar.getChildren().contains(btnStatistics)) {
            return;
        }

        int size = sideBar.getChildren().size();
        sideBar.getChildren().add(size - 1, btnExit);
        sideBar.getChildren().add(size - 2, btnStatistics);
    }
}
