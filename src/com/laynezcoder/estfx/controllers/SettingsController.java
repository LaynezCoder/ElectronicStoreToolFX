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

import com.jfoenix.controls.JFXProgressBar;
import com.laynezcoder.estfx.alerts.AlertType;
import com.laynezcoder.estfx.alerts.AlertsBuilder;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.constants.Constants;
import com.laynezcoder.estfx.constants.Messages;
import com.laynezcoder.estfx.constants.ResourcesPackages;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.database.UserStatistics;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.UserSession;
import com.laynezcoder.estfx.models.Users;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.util.EstfxUtil;
import com.laynezcoder.estfx.util.HistoyBox;
import com.laynezcoder.estfx.util.JFXDialogTool;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SettingsController implements Initializable {

    private final UserSession SESSION = UserSession.getInstace();

    private final double IMPACT_AVERAGE = 0.2;

    @FXML
    private StackPane stckSettings;

    @FXML
    private AnchorPane rootSettings;

    @FXML
    private ScrollPane generalInformationContainer;

    @FXML
    private ScrollPane statisticsContainer;

    @FXML
    private JFXProgressBar progressbarCustomers;

    @FXML
    private JFXProgressBar progressbarQuotes;

    @FXML
    private JFXProgressBar progressbarProducts;

    @FXML
    private JFXProgressBar progressbarSales;

    @FXML
    private ImageView endImage;

    @FXML
    private ImageView imageProfile;

    @FXML
    private Region verifiedIcon;

    @FXML
    private Text biography;

    @FXML
    private Text name;

    @FXML
    private Text textTotalCustomers;

    @FXML
    private Text textTotalQuotes;

    @FXML
    private Text textTotalProducts;

    @FXML
    private Text textTotalSales;

    @FXML
    private Text textRaisedMoney;

    @FXML
    private Text textCustomerImpact;

    @FXML
    private Text textQuotesImpact;

    @FXML
    private Text textProductsImpact;

    @FXML
    private Text textSalesImpact;

    @FXML
    private Text textRaisedMoneyImpact;

    @FXML
    private Text textInsertionDate;

    @FXML
    private MaterialDesignIconView iconCustomerImpact;

    @FXML
    private MaterialDesignIconView iconQuotesImpact;

    @FXML
    private MaterialDesignIconView iconProductsImpact;

    @FXML
    private MaterialDesignIconView iconSalesImpact;

    @FXML
    private MaterialDesignIconView iconRaisedMoneyImpact;

    @FXML
    private BarChart barChart;

    @FXML
    private PieChart pieChartThisWeek;

    @FXML
    private PieChart pieChartLastWeek;

    @FXML
    private Hyperlink linkProfile;

    @FXML
    private HBox pieChartThisWeekContainer;

    @FXML
    private HBox pieChartLastWeekContainer;

    @FXML
    private VBox historyContainer;

    @FXML
    private VBox recognitionButtonsContainer;

    @FXML
    private Button btnCustomersHystory;

    @FXML
    private Button btnQuotesHistory;

    @FXML
    private Button btnProductsHistory;

    @FXML
    private Button btnSalesHistory;

    @FXML
    private Button btnStarSeller;

    @FXML
    private Button btnActiveUser;

    @FXML
    private Button btnValuableUser;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtURL;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private TextArea txtDescription;

    private JFXDialogTool dialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setMask();
        animateNodes();
        selectText();
        init(SESSION.getId());
    }

    @FXML
    private void showDialog() {
        rootSettings.setEffect(Constants.BOX_BLUR_EFFECT);

        dialog = new JFXDialogTool(dialogContainer, stckSettings);
        dialog.show();
        setDataToControls();

        dialog.setOnDialogClosed(ev -> {
            if (!AlertsBuilder.isVisible()) {
                rootSettings.setEffect(null);
            }
        });
    }

    @FXML
    private void closeDialog() {
        if (dialog != null) {
            dialog.close();
        }
    }

    public void init(int id) {
        //We set the values to the graphs and progress bars
        setStatistics(id);

        //Load data
        loadData(id);

        //We set the image of the end of the history
        Image randomImage = ResourcesPackages.getRandomSuccesImage();
        endImage.setImage(randomImage);

        //Initialize the customer history button
        setCustomersHistory(id);
        btnCustomersHystory.setDisable(true);

        //Initialize all buttons
        setHistoryActions(id);
    }

    private void setDataToControls() {
        txtName.setText(SESSION.getName());
        txtUsername.setText(SESSION.getUsername());
        txtPassword.setText(SESSION.getPassword());
        txtConfirmPassword.setText(SESSION.getPassword());
        txtURL.setText(SESSION.getLinkProfile());
        txtDescription.setText(SESSION.getBiography());
    }

    private void updateSession(Users user) {
        SESSION.setName(user.getName());
        SESSION.setUsername(user.getUsername());
        SESSION.setPassword(user.getPassword());
        SESSION.setBiography(user.getBiography());
        SESSION.setLinkProfile(user.getLinkProfile());
    }

    private void loadData(int id) {
        //We establish values of the logged in user
        String username = EstfxUtil.trimText(SESSION.getUsername(), 16);
        name.setText(username);

        if (!SESSION.getUserType().equals("Administrator")) {
            verifiedIcon.setVisible(false);
        }

        biography.setText(SESSION.getBiography());
        linkProfile.setText(SESSION.getLinkProfile());
        EstfxUtil.openBrowser(linkProfile.getText(), linkProfile);

        //We establish the date that the user joined the database
        String dateOfAdmission = UserStatistics.getInsertionDate(id);
        String message = "¡" + username + " has joined the team on " + dateOfAdmission + "!";
        textInsertionDate.setText(message);
    }

    private void setStatistics(int id) {
        double totalCustomers = DatabaseHelper.getCustomers();
        double totalQuotes = DatabaseHelper.getQuotes();
        double totalProducts = DatabaseHelper.getProducts();

        double partialCustomers = UserStatistics.getCustomersAdded(id);
        double partialQuotes = UserStatistics.getQuotesAdded(id);
        double partialProducts = UserStatistics.getProductsAdded(id);

        double lastWeekCustomers = UserStatistics.getCustomersAddedFromLastWeek(id);
        double lastWeekQuotes = UserStatistics.getQuotesAddedFromLastWeek(id);
        double lastWeekProducts = UserStatistics.getProductsAddedFromLastWeek(id);

        double thisWeekCustomers = UserStatistics.getCustomersAddedFromThisWeek(id);
        double thisWeekQuotes = UserStatistics.getQuotesAddedFromThisWeek(id);
        double thisWeekProducts = UserStatistics.getProductsAddedFromThisWeek(id);

        double customersInDecimals = ((partialCustomers * 100) / totalCustomers) / 100;
        double quotesInDecimals = ((partialQuotes * 100) / totalQuotes) / 100;
        double productsInDecimals = ((partialProducts * 100) / totalProducts) / 100;

        if (partialProducts < 50) {
            recognitionButtonsContainer.getChildren().remove(btnStarSeller);
        }

        if (UserStatistics.getSessionsStarted(id) < 100) {
            recognitionButtonsContainer.getChildren().remove(btnActiveUser);
        }

        if (partialCustomers < 50 && partialQuotes < 50 && partialProducts < 50) {
            recognitionButtonsContainer.getChildren().remove(btnValuableUser);
        }

        setIconsFromImpact(customersInDecimals, iconCustomerImpact);
        setIconsFromImpact(quotesInDecimals, iconQuotesImpact);
        setIconsFromImpact(productsInDecimals, iconProductsImpact);

        Animations.progressAnimation(progressbarCustomers, customersInDecimals);
        Animations.progressAnimation(progressbarQuotes, quotesInDecimals);
        Animations.progressAnimation(progressbarProducts, productsInDecimals);
        Animations.progressAnimation(progressbarSales, 0.7);

        textTotalCustomers.setText(String.valueOf((int) partialCustomers));
        textTotalQuotes.setText(String.valueOf((int) partialQuotes));
        textTotalProducts.setText(String.valueOf((int) partialProducts));

        textCustomerImpact.setText((int) (customersInDecimals * 100) + "%");
        textQuotesImpact.setText((int) (quotesInDecimals * 100) + "%");
        textProductsImpact.setText((int) (productsInDecimals * 100) + "%");

        ObservableList<PieChart.Data> pieChartDataThisWeek = FXCollections.observableArrayList();

        PieChart.Data customersThisWeek = new PieChart.Data("Customers", thisWeekCustomers);
        pieChartDataThisWeek.add(customersThisWeek);
        pieChartThisWeek.setData(pieChartDataThisWeek);
        Animations.hover(customersThisWeek.getNode(), 50, 1.1);

        PieChart.Data quotesThisWeek = new PieChart.Data("Quotes", thisWeekQuotes);
        pieChartDataThisWeek.add(quotesThisWeek);
        pieChartThisWeek.setData(pieChartDataThisWeek);
        Animations.hover(quotesThisWeek.getNode(), 50, 1.1);

        PieChart.Data productsThisWeek = new PieChart.Data("Products", thisWeekProducts);
        pieChartDataThisWeek.add(productsThisWeek);
        pieChartThisWeek.setData(pieChartDataThisWeek);
        Animations.hover(productsThisWeek.getNode(), 50, 1.1);

        ObservableList<PieChart.Data> pieChartDataLastWeek = FXCollections.observableArrayList();

        PieChart.Data customersLastWeek = new PieChart.Data("Customers", lastWeekCustomers);
        pieChartDataLastWeek.add(customersLastWeek);
        pieChartLastWeek.setData(pieChartDataLastWeek);
        Animations.hover(customersLastWeek.getNode(), 50, 1.1);

        PieChart.Data quotesLastWeek = new PieChart.Data("Quotes", lastWeekQuotes);
        pieChartDataLastWeek.add(quotesLastWeek);
        pieChartLastWeek.setData(pieChartDataLastWeek);
        Animations.hover(quotesLastWeek.getNode(), 50, 1.1);

        PieChart.Data productsLastWeek = new PieChart.Data("Products", lastWeekProducts);
        pieChartDataLastWeek.add(productsLastWeek);
        pieChartLastWeek.setData(pieChartDataLastWeek);
        Animations.hover(productsLastWeek.getNode(), 50, 1.1);

        XYChart.Series piechartData = new XYChart.Series();
        piechartData.getData().add(new XYChart.Data("Customers", partialCustomers));
        piechartData.getData().add(new XYChart.Data("Quotes", partialQuotes));
        piechartData.getData().add(new XYChart.Data("Products", 10));

        barChart.getData().add(piechartData);

        Image image = ResourcesPackages.getRandomErrorImage();
        if (thisWeekCustomers == 0 && thisWeekQuotes == 0 && thisWeekProducts == 0) {
            pieChartThisWeekContainer.getChildren().remove(pieChartThisWeek);
            pieChartThisWeekContainer.getChildren().addAll(new ImageView(image));
        }

        if (lastWeekCustomers == 0 && lastWeekQuotes == 0 && lastWeekProducts == 0) {
            pieChartLastWeekContainer.getChildren().remove(pieChartLastWeek);
            pieChartLastWeekContainer.getChildren().addAll(new ImageView(image));
        }
    }

    private void setIconsFromImpact(double valueToCompare, MaterialDesignIconView icon) {
        if (valueToCompare >= IMPACT_AVERAGE) {
            icon.getStyleClass().add("icon-treding-up");
            icon.setIcon(MaterialDesignIcon.TRENDING_UP);
        } else {
            icon.getStyleClass().add("icon-treding-down");
            icon.setIcon(MaterialDesignIcon.TRENDING_DOWN);
        }
    }

    @FXML
    private void saveUserInformation() {
        String fullname = txtName.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();
        String url = txtURL.getText().trim();
        String bio = txtDescription.getText().trim();

        if (fullname.isEmpty()) {
            txtName.requestFocus();
            Animations.shake(txtName);
            return;
        }

        if (username.isEmpty()) {
            txtUsername.requestFocus();
            Animations.shake(txtUsername);
            return;
        }

        if (username.length() < 4) {
            txtUsername.requestFocus();
            Animations.shake(txtUsername);
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ENTER_AT_LEAST_4_CHARACTERES);
            return;
        }

        if (password.isEmpty()) {
            txtPassword.requestFocus();
            Animations.shake(txtPassword);
            return;
        }

        if (password.length() < 4) {
            txtPassword.requestFocus();
            Animations.shake(txtPassword);
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ENTER_AT_LEAST_4_CHARACTERES);
            return;
        }

        if (confirmPassword.isEmpty()) {
            txtConfirmPassword.requestFocus();
            Animations.shake(txtConfirmPassword);
            return;
        }

        if (!confirmPassword.equals(password)) {
            txtConfirmPassword.requestFocus();
            Animations.shake(txtPassword);
            Animations.shake(txtConfirmPassword);
            NotificationsBuilder.create(NotificationType.ERROR, Messages.PASSWORDS_NOT_MATCH);
            return;
        }

        if (url.isEmpty()) {
            Animations.shake(txtURL);
            return;
        }

        if (!EstfxUtil.validateURL(url)) {
            Animations.shake(txtURL);
            NotificationsBuilder.create(NotificationType.ERROR, Messages.INVALID_URL);
            return;
        }

        if (bio.isEmpty()) {
            txtDescription.requestFocus();
            Animations.shake(txtDescription);
            return;
        }

        if (DatabaseHelper.checkIfUserExists(username) != 0 && !username.equals(UserSession.getInstace().getUsername())) {
            txtUsername.requestFocus();
            Animations.shake(txtUsername);
            NotificationsBuilder.create(NotificationType.ERROR, Messages.USER_ALREADY_EXISTS);
            return;
        }

        Users user = new Users();
        user.setId(SESSION.getId());
        user.setName(fullname);
        user.setUsername(username);
        user.setPassword(password);
        user.setLinkProfile(url);
        user.setBiography(bio);

        boolean result = DatabaseHelper.updateUserInformation(user);
        if (result) {
            closeDialog();
            updateSession(user);
            loadData(SESSION.getId());
            AlertsBuilder.create(AlertType.SUCCES, stckSettings, rootSettings, rootSettings, Messages.ADDED_RECORD);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }
    }

    private void setHistoryActions(int id) {
        btnCustomersHystory.setOnAction(ev -> {
            disableButtons(ev);
            setCustomersHistory(id);
        });

        btnQuotesHistory.setOnAction(ev -> {
            disableButtons(ev);
            setQuotesHistory(id);
        });
    }

    private void setCustomersHistory(int id) {
        historyContainer.getChildren().clear();
        try {
            ResultSet result = UserStatistics.getCustomerHistory(id);
            while (result.next()) {
                String value = EstfxUtil.trimText(result.getString(1), 35);
                String date = result.getDate(2).toString();
                historyContainer.getChildren().addAll(new HistoyBox(HistoyBox.Module.CUSTOMER, date, value));
            }
            Animations.fadeIn(historyContainer);
        } catch (SQLException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setQuotesHistory(int id) {
        historyContainer.getChildren().clear();
        try {
            ResultSet result = UserStatistics.getQuotesHistory(id);
            while (result.next()) {
                String value = EstfxUtil.trimText(result.getString(1), 70);
                String date = result.getDate(2).toString();
                historyContainer.getChildren().addAll(new HistoyBox(HistoyBox.Module.QUOTE, date, value));
            }
            Animations.fadeIn(historyContainer);
        } catch (SQLException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void disableButtons(ActionEvent event) {
        EstfxUtil.disableNode(event, btnCustomersHystory);
        EstfxUtil.disableNode(event, btnQuotesHistory);
        EstfxUtil.disableNode(event, btnProductsHistory);
        EstfxUtil.disableNode(event, btnSalesHistory);
    }

    private void animateNodes() {
        Animations.bounceIn(verifiedIcon);
        Animations.fadeInUp(generalInformationContainer);
        Animations.fadeInUp(statisticsContainer);
        Animations.imageTransition(endImage).play();
    }

    private void selectText() {
        TextFieldMask.selectText(txtName);
        TextFieldMask.selectText(txtUsername);
        TextFieldMask.selectText(txtPassword);
        TextFieldMask.selectText(txtConfirmPassword);
    }

    private void setMask() {
        TextFieldMask.onlyLetters(txtName, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtUsername, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtPassword, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtConfirmPassword, 40);
    }
}
