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

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXToggleButton;
import com.laynezcoder.estfx.alerts.AlertType;
import com.laynezcoder.estfx.alerts.AlertsBuilder;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.Customers;
import com.laynezcoder.estfx.models.Quotes;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.constants.Constants;
import com.laynezcoder.estfx.constants.Messages;
import com.laynezcoder.estfx.constants.QuotationStatus;
import com.laynezcoder.estfx.constants.ResourcesPackages;
import com.laynezcoder.estfx.util.AutocompleteComboBox;
import com.laynezcoder.estfx.util.ContextMenu;
import com.laynezcoder.estfx.util.ExpandTextArea;
import com.laynezcoder.estfx.util.JFXDialogTool;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class QuotesController implements Initializable {

    private ObservableList<Quotes> listQuotes;

    private ObservableList<Customers> listCustomers;

    @FXML
    private StackPane stckQuotes;

    @FXML
    private AnchorPane rootQuotes;

    @FXML
    private HBox tableContainer;

    @FXML
    private TableView<Quotes> tblQuotes;

    @FXML
    private TableColumn<Quotes, Integer> colId;

    @FXML
    private TableColumn<Quotes, String> colDescription;

    @FXML
    private TableColumn<Quotes, Double> colPrice;

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
    private HBox searchContainer;

    @FXML
    private VBox containerAdd;

    @FXML
    private VBox containerDelete;

    @FXML
    private ImageView imageDelete;

    @FXML
    private TextField txtSearchCustomer;

    @FXML
    private TextField txtSearchDescription;

    @FXML
    private TextField txtPrice;

    @FXML
    private DatePicker dtpDate;

    @FXML
    private TextArea txtDescription;

    @FXML
    private HBox buttonsContainer;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<Customers> cmbIdCustomer;

    @FXML
    private Text titleAdd;

    @FXML
    private JFXToggleButton toggleButtonExists;

    @FXML
    private JFXToggleButton toggleButtonReport;

    @FXML
    private JFXToggleButton toggleButtonRealized;

    private JFXDialogTool dialogAdd;

    private JFXDialogTool dialogDelete;

    private ContextMenu contextMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComboBox();
        deleteUserDeleteKey();
        loadData();
        setMask();
        setContextMenu();
        animateNodes();
        selectText();
        imageDelete.setImage(ResourcesPackages.DELETE_IMAGE);
    }

    private void setContextMenu() {
        contextMenu = new ContextMenu(tblQuotes);

        contextMenu.setActionEdit(ev -> {
            showDialogEditQuote();
            contextMenu.hide();
        });

        contextMenu.setActionDelete(ev -> {
            showDialogDelete();
            contextMenu.hide();
        });

        contextMenu.setActionDetails(ev -> {
            showDialogDetailsQuote();
            contextMenu.hide();
        });

        contextMenu.setActionRefresh(ev -> {
            loadData();
            contextMenu.hide();
        });

        contextMenu.display();
    }

    private void initializeComboBox() {
        loadComboBox();
        AutocompleteComboBox.autoCompleteComboBoxPlus(cmbIdCustomer, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.toString().equals(typedText));
    }

    private void animateNodes() {
        Animations.fadeInUp(searchContainer);
        Animations.fadeInUp(tableContainer);
    }

    private void selectText() {
        TextFieldMask.selectText(txtSearchCustomer);
        TextFieldMask.selectText(txtPrice);
        TextFieldMask.selectText(cmbIdCustomer.getEditor());
    }

    private void setMask() {
        TextFieldMask.onlyDoubleNumbers5Integers(txtPrice);
        TextFieldMask.setTextIfFieldIsEmpty(txtPrice);
    }

    @FXML
    private void showDialogAdd() {
        enableEditControls();
        tblQuotes.setDisable(true);
        titleAdd.setText("Add Quote");
        cmbIdCustomer.setDisable(false);
        rootQuotes.setEffect(Constants.BOX_BLUR_EFFECT);

        if (!buttonsContainer.getChildren().contains(btnSave)) {
            buttonsContainer.getChildren().add(btnSave);
        }
        btnSave.setDisable(false);
        buttonsContainer.getChildren().remove(btnUpdate);

        dialogAdd = new JFXDialogTool(containerAdd, stckQuotes);
        dialogAdd.show();

        dialogAdd.setOnDialogOpened(ev -> {
            txtPrice.requestFocus();
        });

        dialogAdd.setOnDialogClosed(ev -> {
            if (!AlertsBuilder.isVisible()) {
                tblQuotes.setDisable(false);
                rootQuotes.setEffect(null);
                cleanControls();
            }
        });

        dtpDate.getEditor().setDisable(true);
        dtpDate.focusedProperty().addListener((o, oldV, newV) -> {
            if (dtpDate.getEditor().getText().isEmpty() && newV) {
                dtpDate.setValue(LocalDate.now());
            }
        });

        cmbIdCustomer.focusedProperty().addListener((o, oldV, newV) -> {
            if (newV) {
                cmbIdCustomer.show();
            }
        });
    }

    @FXML
    private void closeDialogAdd() {
        dialogAdd.close();
    }

    private void showDialogDelete() {
        if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Messages.NO_RECORD_SELECTED);
            return;
        }

        tblQuotes.setDisable(true);
        rootQuotes.setEffect(Constants.BOX_BLUR_EFFECT);

        dialogDelete = new JFXDialogTool(containerDelete, stckQuotes);
        dialogDelete.show();

        dialogDelete.setOnDialogClosed(ev -> {
            if (!AlertsBuilder.isVisible()) {
                tblQuotes.setDisable(false);
                rootQuotes.setEffect(null);
            }
        });
    }

    @FXML
    private void closeDialogDelete() {
        if (dialogDelete != null) {
            dialogDelete.close();
        }
    }

    @FXML
    private void showDialogEditQuote() {
        if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Messages.NO_RECORD_SELECTED);
            return;
        }

        showDialogAdd();
        selectedRecord();
        titleAdd.setText("Update quote");
        cmbIdCustomer.setDisable(true);

        if (!buttonsContainer.getChildren().contains(btnUpdate)) {
            buttonsContainer.getChildren().add(btnUpdate);
        }
        buttonsContainer.getChildren().remove(btnSave);
    }

    @FXML
    private void showDialogDetailsQuote() {
        if (tblQuotes.getSelectionModel().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Messages.NO_RECORD_SELECTED);
            return;
        }

        showDialogAdd();
        titleAdd.setText("Quotes details");
        cmbIdCustomer.setDisable(true);
        btnSave.setDisable(true);
        disableEditControls();
        selectedRecord();
    }

    private void selectedRecord() {
        Quotes quotes = tblQuotes.getSelectionModel().getSelectedItem();
        txtPrice.setText(String.valueOf(quotes.getPrice()));
        dtpDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(quotes.getRequestDate())));
        txtDescription.setText(quotes.getDescription());
        cmbIdCustomer.getSelectionModel().select(DatabaseHelper.searchCustomer(quotes.getCustomerName()));
        
        toggleButtonExists.setText(quotes.getExistence());
        toggleButtonRealized.setText(quotes.getRealization());
        toggleButtonReport.setText(quotes.getReport());

        if (quotes.getExistence().equals(QuotationStatus.EXISTENT.value())) {
            toggleButtonExists.selectedProperty().set(true);
        } else {
            toggleButtonExists.selectedProperty().set(false);
        }

        if (quotes.getRealization().equals(QuotationStatus.REALIZED.value())) {
            toggleButtonRealized.selectedProperty().set(true);
        } else {
            toggleButtonRealized.selectedProperty().set(false);
        }

        if (quotes.getReport().equals(QuotationStatus.REPORTED.value())) {
            toggleButtonReport.selectedProperty().set(true);
        } else {
            toggleButtonReport.selectedProperty().set(false);
        }
    }

    @FXML
    private void setActionToggleButton() {
        if (toggleButtonExists.isSelected()) {
            toggleButtonExists.setText(QuotationStatus.EXISTENT.value());
        } else {
            toggleButtonExists.setText(QuotationStatus.NOT_EXISTENT.value());
        }

        if (toggleButtonRealized.isSelected()) {
            toggleButtonRealized.setText(QuotationStatus.REALIZED.value());
        } else {
            toggleButtonRealized.setText(QuotationStatus.NOT_REALIZED.value());
        }

        if (toggleButtonReport.isSelected()) {
            toggleButtonReport.setText(QuotationStatus.REPORTED.value());
        } else {
            toggleButtonReport.setText(QuotationStatus.NOT_REPORTED.value());
        }
    }

    @FXML
    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colExistence.setCellValueFactory(new ButtonExistsCellValueFactory());
        colReport.setCellValueFactory(new ButtonReportCellValueFactory());
        colRealization.setCellValueFactory(new ButtonRealizedCellValueFactory());
    }

    private void loadTable() {
        ArrayList<Quotes> list = new ArrayList<>();
        try {
            String sql = "SELECT q.id, q.descriptionQuote, q.requestDate, q.price, q.existence, q.realization, q.report, c.customerName\nFROM Quotes AS q\n"
                    + "INNER JOIN Customers AS c ON q.customerId = c.id";
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
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Messages.ERROR_CONNECTION_MYSQL);
        }
        listQuotes = FXCollections.observableArrayList(list);
        tblQuotes.setItems(listQuotes);
        tblQuotes.setFixedCellSize(30);
    }

    private void loadComboBox() {
        ArrayList<Customers> list = new ArrayList<>();
        try {
            String sql = "SELECT id, customerName FROM Customers";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String customerName = resultSet.getString("customerName");
                list.add(new Customers(id, customerName));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        listCustomers = FXCollections.observableArrayList(list);
        cmbIdCustomer.setItems(listCustomers);
    }

    @FXML
    private void newQuote() {
        String price = txtPrice.getText().trim();
        String description = txtDescription.getText().trim();

        if (dtpDate.getEditor().getText().isEmpty()) {
            Animations.shake(dtpDate);
            return;
        }

        if (cmbIdCustomer.getSelectionModel().isEmpty()) {
            Animations.shake(cmbIdCustomer);
            return;
        }

        if (description.isEmpty()) {
            new Shake(txtDescription).play();
            return;
        }

        Quotes quotes = new Quotes();

        if (price.isEmpty()) {
            quotes.setPrice(0.0);
        } else {
            quotes.setPrice(Double.valueOf(price));
        }

        if (toggleButtonExists.isSelected()) {
            quotes.setExistence(QuotationStatus.EXISTENT.value());
        } else {
            quotes.setExistence(QuotationStatus.NOT_EXISTENT.value());
        }

        if (toggleButtonRealized.isSelected()) {
            quotes.setRealization(QuotationStatus.REALIZED.value());
        } else {
            quotes.setRealization(QuotationStatus.NOT_REALIZED.value());
        }

        if (toggleButtonReport.isSelected()) {
            quotes.setReport(QuotationStatus.REPORTED.value());
        } else {
            quotes.setReport(QuotationStatus.NOT_REPORTED.value());
        }

        quotes.setDescription(description);
        quotes.setRequestDate(java.sql.Date.valueOf(dtpDate.getValue()));
        quotes.setCustomerId(cmbIdCustomer.getSelectionModel().getSelectedIndex());

        boolean result = DatabaseHelper.insertNewQuote(quotes);
        if (result) {
            loadData();
            cleanControls();
            closeDialogAdd();
            AlertsBuilder.create(AlertType.SUCCES, stckQuotes, rootQuotes, tblQuotes, Messages.ADDED_RECORD);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }
    }

    @FXML
    private void deleteQuotes() {
        boolean result = DatabaseHelper.deeleteQuotes(tblQuotes);
        if (result) {
            loadData();
            closeDialogDelete();
            AlertsBuilder.create(AlertType.SUCCES, stckQuotes, rootQuotes, tblQuotes, Messages.DELETED_RECORD);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }
    }

    @FXML
    private void updateQuotes() {
        String price = txtPrice.getText().trim();
        String description = txtDescription.getText().trim();

        if (dtpDate.getEditor().getText().isEmpty()) {
            Animations.shake(dtpDate);
            return;
        }

        if (description.isEmpty()) {
            new Shake(txtDescription).play();
            return;
        }

        Quotes quotes = tblQuotes.getSelectionModel().getSelectedItem();

        if (price.isEmpty()) {
            quotes.setPrice(0.0);
        } else {
            quotes.setPrice(Double.valueOf(price));
        }

        if (toggleButtonExists.isSelected()) {
            quotes.setExistence(QuotationStatus.EXISTENT.value());
        } else {
            quotes.setExistence(QuotationStatus.NOT_EXISTENT.value());
        }

        if (toggleButtonRealized.isSelected()) {
            quotes.setRealization(QuotationStatus.REALIZED.value());
        } else {
            quotes.setRealization(QuotationStatus.NOT_REALIZED.value());
        }

        if (toggleButtonReport.isSelected()) {
            quotes.setReport(QuotationStatus.REPORTED.value());
        } else {
            quotes.setReport(QuotationStatus.NOT_REPORTED.value());
        }

        quotes.setDescription(description);
        quotes.setRequestDate(java.sql.Date.valueOf(dtpDate.getValue()));
        quotes.setId(quotes.getId());

        boolean result = DatabaseHelper.updateQuotes(quotes);
        if (result) {
            loadData();
            cleanControls();
            closeDialogAdd();
            AlertsBuilder.create(AlertType.SUCCES, stckQuotes, rootQuotes, tblQuotes, Messages.UPDATED_RECORD);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }
    }

    private void deleteUserDeleteKey() {
        rootQuotes.setOnKeyPressed(ev -> {
            if (ev.getCode().equals(KeyCode.DELETE)) {
                if (contextMenu.isShowing()) {
                    contextMenu.hide();
                }

                if (tblQuotes.isDisable()) {
                    return;
                }

                if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
                    AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Messages.NO_RECORD_SELECTED);
                    return;
                }

                deleteQuotes();
            }
        });
    }

    @FXML
    private void expandTextArea() {
        Stage owner = (Stage) txtDescription.getScene().getWindow();
        ExpandTextArea expand = new ExpandTextArea(owner, txtDescription);
        expand.show();
    }

    private void cleanControls() {
        cmbIdCustomer.getSelectionModel().clearSelection();
        txtDescription.clear();
        dtpDate.setValue(null);
        txtPrice.clear();
        toggleButtonExists.selectedProperty().set(false);
        toggleButtonRealized.selectedProperty().set(false);
        toggleButtonReport.selectedProperty().set(false);
    }

    private void disableEditControls() {
        cmbIdCustomer.setEditable(false);
        txtDescription.setEditable(false);
        txtPrice.setEditable(false);
        dtpDate.setEditable(false);
        toggleButtonExists.setDisable(true);
        toggleButtonRealized.setDisable(true);
        toggleButtonReport.setDisable(true);
    }

    private void enableEditControls() {
        cmbIdCustomer.setEditable(true);
        txtDescription.setEditable(true);
        txtPrice.setEditable(true);
        dtpDate.setEditable(true);
        toggleButtonExists.setDisable(false);
        toggleButtonRealized.setDisable(false);
        toggleButtonReport.setDisable(false);
    }

    @FXML
    private void searchCustomer() {
        ObservableList<Quotes> list = FXCollections.observableArrayList();

        String customer = txtSearchCustomer.getText().trim();
        if (customer.isEmpty()) {
            tblQuotes.setItems(listQuotes);
        } else {
            list.clear();
            for (Quotes q : listQuotes) {
                if (q.getCustomerName().toLowerCase().contains(customer.toLowerCase())) {
                    list.add(q);
                }
            }
            tblQuotes.setItems(list);
        }
    }

    @FXML
    private void searchDescription() {
        ObservableList<Quotes> list = FXCollections.observableArrayList();

        String description = txtSearchDescription.getText().trim();
        if (description.isEmpty()) {
            tblQuotes.setItems(listQuotes);
        } else {
            list.clear();
            for (Quotes q : listQuotes) {
                if (q.getDescription().toLowerCase().contains(description.toLowerCase())) {
                    list.add(q);
                }
            }
            tblQuotes.setItems(list);
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
