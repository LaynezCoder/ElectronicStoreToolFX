package com.laynezcoder.estfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.resources.Constants;
import com.laynezcoder.resources.Resources;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {

    @FXML
    private StackPane stckMain;

    @FXML
    private JFXButton btnHome;

    @FXML
    private JFXButton btnCustomers;

    @FXML
    private JFXButton btnQuotes;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnAbout;

    @FXML
    private JFXButton btnStatistics;

    @FXML
    private JFXButton btnAddUser;

    @FXML
    private JFXButton btnSettings;

    @FXML
    private JFXButton btnProducts;

    @FXML
    private AnchorPane rootContainer;

    @FXML
    private AnchorPane rootSideMenu;

    @FXML
    private AnchorPane tooltipCustomers;

    @FXML
    private AnchorPane tooltipHome;

    @FXML
    private AnchorPane tooltipQuotes;

    @FXML
    private AnchorPane tooltipSettings;

    @FXML
    private AnchorPane tooltipExit;

    @FXML
    private AnchorPane tooltipProducts;

    @FXML
    private AnchorPane tooltipAbout;

    @FXML
    private AnchorPane tooltipStatistics;

    @FXML
    private AnchorPane tooltipAddUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeWindowsInitialize();
        tooltips();
    }

    @FXML
    private void setDisableButtons(MouseEvent event) {
        setDisableButtons(event, btnHome);
        setDisableButtons(event, btnCustomers);
        setDisableButtons(event, btnQuotes);
        setDisableButtons(event, btnProducts);
        setDisableButtons(event, btnAddUser);
        setDisableButtons(event, btnStatistics);
        setDisableButtons(event, btnAbout);
        setDisableButtons(event, btnSettings);
        setDisableButtons(event, btnExit);
    }

    private void homeWindowsInitialize() {
        btnHome.setDisable(true);
        showFXMLWindows("HomeView");
    }

    @FXML
    private void homeWindows(MouseEvent event) {
        btnHome.setDisable(true);
        showFXMLWindows("HomeView");
        setDisableButtons(event);
    }

    @FXML
    private void customersWindows(MouseEvent event) {
        showFXMLWindows("CustomersView");
        setDisableButtons(event);
    }

    @FXML
    private void quotesWindows(MouseEvent event) {
        showFXMLWindows("QuotesView");
        setDisableButtons(event);
    }

    @FXML
    private void settingsWindows(MouseEvent event) {
        showFXMLWindows("SettingsView");
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
    private void productsWindows(MouseEvent event) { 
        showFXMLWindows("ProductsView");
        setDisableButtons(event);
    }

    @FXML
    private void addUserWindows(MouseEvent event) {
        showFXMLWindows("UsersView");
        setDisableButtons(event);
    }

    @FXML
    private void loginWindow() {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            Parent root = FXMLLoader.load(getClass().getResource(Constants.LOGIN_VIEW));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getIcons().add(new Image(Constants.STAGE_ICON));
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

    private void setDisableButtons(MouseEvent event, JFXButton button) {
        if (event.getSource() == button) {
            button.setDisable(true);
        } else {
            button.setDisable(false);
        }
    }

    private void tooltips() {
        Resources.tooltip(btnHome, tooltipHome);
        Resources.tooltip(btnCustomers, tooltipCustomers);
        Resources.tooltip(btnQuotes, tooltipQuotes);
        Resources.tooltip(btnSettings, tooltipSettings);
        Resources.tooltip(btnExit, tooltipExit);
        Resources.tooltip(btnProducts, tooltipProducts);
        Resources.tooltip(btnStatistics, tooltipStatistics);
        Resources.tooltip(btnAbout, tooltipAbout);
        Resources.tooltip(btnAddUser, tooltipAddUser);
    }

    private void showFXMLWindows(String FXMLName) {
        rootContainer.getChildren().clear();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Constants.MEDIA_PACKAGE + FXMLName + ".fxml"));
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            rootContainer.getChildren().setAll(root);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JFXButton getBtnStatistics() {
        return btnStatistics;
    }

    public JFXButton getBtnAddUser() {
        return btnAddUser;
    }

    public JFXButton getBtnAbout() {
        return btnAbout;
    }
}
