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
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.Quotes;
import com.laynezcoder.estfx.constants.Messages;
import com.laynezcoder.estfx.constants.QuotationStatus;
import com.laynezcoder.estfx.constants.ResourcesPackages;
import com.laynezcoder.estfx.models.UserSession;
import com.laynezcoder.estfx.util.EstfxUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class HomeController implements Initializable {

    private final String IMAGE = ResourcesPackages.UI_IMAGES_PACKAGE + "character.png";

    private final String DEFAULT_WELCOME_TEXT = "¿What do you think if you start adding a new client?";

    private ObservableList<Quotes> listQuotes;

    private ObservableList<Quotes> filterQuotes;

    @FXML
    private StackPane stckHome;

    @FXML
    private HBox searchContainer;

    @FXML
    private AnchorPane rootHome;

    @FXML
    private TextField txtSearchRecentQuotes;

    @FXML
    private HBox welcomeContainer;

    @FXML
    private Text textDescriptionWelcome;

    @FXML
    private Text textWelcome;

    @FXML
    private Text totalCustomers;

    @FXML
    private Text totalQuotes;

    @FXML
    private Text totalProducts;

    @FXML
    private Text nowQuotes;

    @FXML
    private ImageView image;

    @FXML
    private JFXProgressBar progressBarCustomers;

    @FXML
    private JFXProgressBar progressBarQuotes;

    @FXML
    private JFXProgressBar progressBarProducts;

    @FXML
    private JFXProgressBar progressBarRecentQuotes;

    @FXML
    private HBox statisticsContainer;

    @FXML
    private TableView<Quotes> tblQuotes;

    @FXML
    private TableColumn<Quotes, Integer> colId;

    @FXML
    private TableColumn<Quotes, String> colDescription;

    @FXML
    private TableColumn<Quotes, String> colPrice;

    @FXML
    private TableColumn<Quotes, String> colDate;

    @FXML
    private TableColumn<Quotes, String> colCustomerName;

    @FXML
    private TableColumn<Quotes, FontAwesomeIconView> colExistence;

    @FXML
    private TableColumn<Quotes, FontAwesomeIconView> colRealization;

    @FXML
    private TableColumn<Quotes, FontAwesomeIconView> colReport;

    @FXML
    private HBox tableContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animationsNodes();
        setWelcomeText();
        counterRecords();
        loadData();
        selectText();
        animateProgressBar();
        filterQuotes = FXCollections.observableArrayList();
        image.setImage(new Image(IMAGE, 170, 130, true, true));
    }

    private void selectText() {
        TextFieldMask.selectText(txtSearchRecentQuotes);
    }

    private void animateProgressBar() {
        Animations.progressAnimation(progressBarCustomers, 0.5);
        Animations.progressAnimation(progressBarQuotes, 0.7);
        Animations.progressAnimation(progressBarProducts, 0.3);
        Animations.progressAnimation(progressBarRecentQuotes, 0.8);
    }

    private void animationsNodes() {
        Animations.fadeInUp(searchContainer);
        Animations.fadeInUp(welcomeContainer);
        Animations.fadeInUp(tableContainer);
        Animations.fadeInUp(statisticsContainer);
    }

    private void counterRecords() {
        try {
            String sql = "SELECT (SELECT COUNT(*) FROM Customers) AS Customers, (SELECT COUNT(*) FROM Products) AS Products";
            PreparedStatement preparedStatetent = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatetent.executeQuery();
            while (rs.next()) {
                totalCustomers.setText(String.valueOf(rs.getInt(1)));
                totalProducts.setText(String.valueOf(rs.getInt(2)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int getTotalQuotes() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM Quotes";
            PreparedStatement preparedStatetent = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatetent.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            totalCustomers.setText(String.valueOf(count));
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    private void setWelcomeText() {
        int total = getTotalQuotes();
        totalQuotes.setText(String.valueOf(total));

        UserSession session = UserSession.getInstace();
        String name = EstfxUtil.trimText(session.getName(), 20);
        switch (total) {
            case 10:
                setText(name, 10);
                break;
            case 20:
                setText(name, 20);
                break;
            case 30:
                setText(name, 30);
                break;
            case 50:
                setText(name, 50);
                break;
            case 100:
                setText(name, 100);
                break;
            case 500:
                setText(name, 500);
                break;
            default:
                textWelcome.setText("¡Welcome back, " + name + "!");
                textDescriptionWelcome.setText(DEFAULT_WELCOME_TEXT);
                break;
        }
    }

    private void setText(String name, int total) {
        textWelcome.setText("¡Congratulations " + name + ", " + total + " new quotes have been registered!");
        textDescriptionWelcome.setText("!Nice job!");
    }

    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colExistence.setCellValueFactory(new ButtonExistsCellValueFactory());
        colRealization.setCellValueFactory(new ButtonRealizedCellValueFactory());
        colReport.setCellValueFactory(new ButtonReportCellValueFactory());
    }

    private void loadTable() {
        ArrayList<Quotes> list = new ArrayList<>();
        try {
            int total = 0;
            String sql = "SELECT q.id, q.descriptionQuote, q.requestDate, q.price, q.existence, q.realization, q.report, c.customerName\nFROM Quotes AS q\n"
                    + "INNER JOIN Customers AS c ON q.customerId = c.id WHERE requestDate = DATE(NOW())";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String descriptionQuote = resultSet.getString("descriptionQuote");
                Date requestDate = resultSet.getDate("requestDate");
                Double price = resultSet.getDouble("price");
                String existence = resultSet.getString("existence");
                String realization = resultSet.getString("realization");
                String report = resultSet.getString("report");
                String customerName = resultSet.getString("customerName");
                list.add(new Quotes(id, descriptionQuote, requestDate, price, existence, realization, report, customerName));
                total++;
            }
            nowQuotes.setText(String.valueOf(total));
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            AlertsBuilder.create(AlertType.ERROR, stckHome, rootHome, rootHome, Messages.ERROR_CONNECTION_MYSQL);
        }
        listQuotes = FXCollections.observableArrayList(list);
        tblQuotes.setItems(listQuotes);
    }

    @FXML
    private void filterQuotes() {
        String recentQuotes = txtSearchRecentQuotes.getText().trim();

        if (recentQuotes.isEmpty()) {
            tblQuotes.setItems(listQuotes);
        } else {
            filterQuotes.clear();
            for (Quotes q : listQuotes) {
                if (q.getDescription().toLowerCase().contains(recentQuotes.toLowerCase())) {
                    filterQuotes.add(q);
                }
            }
            tblQuotes.setItems(filterQuotes);
        }
    }

    private class ButtonExistsCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, FontAwesomeIconView>, ObservableValue<FontAwesomeIconView>> {

        @Override
        public ObservableValue<FontAwesomeIconView> call(TableColumn.CellDataFeatures<Quotes, FontAwesomeIconView> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            if (item.getExistence().equals(QuotationStatus.EXISTENT.value())) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                icon.getStyleClass().add("icon-check");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                icon.getStyleClass().add("icon-error");
            }
            return new SimpleObjectProperty<>(icon);
        }
    }

    private class ButtonReportCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, FontAwesomeIconView>, ObservableValue<FontAwesomeIconView>> {

        @Override
        public ObservableValue<FontAwesomeIconView> call(TableColumn.CellDataFeatures<Quotes, FontAwesomeIconView> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            if (item.getReport().equals(QuotationStatus.REPORTED.value())) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                icon.getStyleClass().add("icon-check");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                icon.getStyleClass().add("icon-error");
            }
            return new SimpleObjectProperty<>(icon);
        }
    }

    private class ButtonRealizedCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, FontAwesomeIconView>, ObservableValue<FontAwesomeIconView>> {

        @Override
        public ObservableValue<FontAwesomeIconView> call(TableColumn.CellDataFeatures<Quotes, FontAwesomeIconView> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            if (item.getRealization().equals(QuotationStatus.REALIZED.value())) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                icon.getStyleClass().add("icon-check");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                icon.getStyleClass().add("icon-error");
            }
            return new SimpleObjectProperty<>(icon);
        }
    }
}
