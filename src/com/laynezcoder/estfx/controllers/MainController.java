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
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.resources.Constants;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {
    
    private final char FIRST_CHARACTER_VIEW = 'V';

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
    private Text windowInformation;

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
            ps.setInt(1, DatabaseHelper.getSessionId());
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

        Circle clip = new Circle(20);
        clip.setCenterX(imageProfile.getFitWidth() / 2);
        clip.setCenterY(imageProfile.getFitHeight() / 2);
        imageProfile.setClip(clip);
    }
    
    private void setInformation(String windowName) {
        for (int i = 0; i < windowName.length(); i++) {
            if(windowName.charAt(i) == FIRST_CHARACTER_VIEW) {
                windowInformation.setText(windowName.substring(0, i));
            }
        }
    }

    @FXML
    private void setDisableButtons(MouseEvent event) {
        setDisableButtons(event, btnHome);
        setDisableButtons(event, btnCustomers);
        setDisableButtons(event, btnQuotes);
        setDisableButtons(event, btnProducts);
        setDisableButtons(event, btnUsers);
        setDisableButtons(event, btnStatistics);
        setDisableButtons(event, btnExit);
    }

    private void homeWindowsInitialize() {
        setInformation("HomeView");
        btnHome.setDisable(true);
        showFXMLWindows("HomeView");
    }

    @FXML
    private void homeWindows(MouseEvent event) {
        setInformation("HomeView");
    }
 
    @FXML
    private void customersWindows(MouseEvent event) {
        setInformation("CustomersView");
    }

    @FXML
    private void quotesWindows(MouseEvent event) {
        showFXMLWindows("QuotesView");
        setDisableButtons(event);
    }

    @FXML
    private void productsWindows(MouseEvent event) {
        showFXMLWindows("ProductsView");
        setDisableButtons(event);
    }

    @FXML
    private void usersWindows(MouseEvent event) {
        showFXMLWindows("UsersView");
        setDisableButtons(event);
    }

    @FXML
    private void statisticsWindows(MouseEvent event) {
        showFXMLWindows("StatisticsView");
        setDisableButtons(event);
    }

    @FXML
    private void aboutWindows(MouseEvent event) {
        showFXMLWindows("AboutView");
        setDisableButtons(event);
    }

    @FXML
    private void settingsWindows(MouseEvent event) {
        showFXMLWindows("SettingsView");
        setDisableButtons(event);
    }

    @FXML
    private void loginWindow() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Constants.LOGIN_VIEW));
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.getIcons().add(new Image(Constants.STAGE_ICON));
            stage.setScene(new Scene(root));
            stage.show();
            closeStage();
            DatabaseHelper.logout();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeStage() {
        ((Stage) btnHome.getScene().getWindow()).close();
    }

    private void setDisableButtons(MouseEvent event, Button button) {
        if (event.getSource().equals(button)) {
            button.setDisable(true);
        } else {
            button.setDisable(false);
        }
    }

    private void showFXMLWindows(String FXMLName) {
        container.getChildren().clear();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Constants.FXML_PACKAGE + FXMLName + ".fxml"));
            AnchorPane.setBottomAnchor(root, 5.0);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            container.getChildren().setAll(root);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
