package com.laynezcoder.estfx.controllers;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.laynezcoder.estfx.alerts.AlertType;
import com.laynezcoder.estfx.alerts.AlertsBuilder;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.mask.RequieredFieldsValidators;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.Customers;
import com.laynezcoder.estfx.models.Quotes;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.resources.Constants;
import com.laynezcoder.estfx.util.AutocompleteComboBox;
import com.laynezcoder.estfx.util.ContextMenu;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class QuotesController implements Initializable {

    private ObservableList<Quotes> listQuotes;

    private ObservableList<Customers> listCustomers;

    private ObservableList<Quotes> filterQuotes;

    @FXML
    private StackPane stckQuotes;

    @FXML
    private AnchorPane rootQuotes;

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
    private TableColumn<Quotes, JFXButton> colExistence;

    @FXML
    private TableColumn<Quotes, JFXButton> colRealization;

    @FXML
    private TableColumn<Quotes, JFXButton> colReport;

    @FXML
    private HBox rootSearchQuotes;

    @FXML
    private AnchorPane containerAddQuotes;

    @FXML
    private AnchorPane containerDeleteQuotes;

    @FXML
    private TextField txtSearchCustomer;

    @FXML
    private TextField txtSearchQuotes;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXDatePicker dtpDate;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private JFXButton btnAddQuotes;

    @FXML
    private JFXButton btnSaveQuotes;

    @FXML
    private JFXButton btnUpdateQuotes;

    @FXML
    private JFXComboBox<Customers> cmbIdCustomer;

    @FXML
    private Text titleWindowAddQuotes;

    @FXML
    private JFXToggleButton toggleButtonExists;

    @FXML
    private JFXToggleButton toggleButtonReport;

    @FXML
    private JFXToggleButton toggleButtonRealized;

    private JFXDialogTool dialogAddQuote;

    private JFXDialogTool dialogDeleteQuote;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filterQuotes = FXCollections.observableArrayList();
        setActionToggleButton();
        initializeComboBox();
        deleteUserDeleteKey();
        closeDialogWithEscapeKey();
        loadData();
        setMask();
        setContextMenu();
        animateNodes();
        setValidations();
        selectText();
        closeDialogWithTextFields();
    }
    
    private void setContextMenu() {
        ContextMenu contextMenu = new ContextMenu(tblQuotes);

        contextMenu.setActionEdit(ev -> {
            showDialogEditQuote();
            contextMenu.hide();
        });

        contextMenu.setActionDelete(ev -> {
            showDialogDeleteQuotes();
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

        contextMenu.show();
    }

    private void initializeComboBox() {
        loadComboBox();
        AutocompleteComboBox.autoCompleteComboBoxPlus(cmbIdCustomer, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.toString().equals(typedText));
    }

    private void animateNodes() {
        Animations.fadeInUp(rootSearchQuotes);
        Animations.fadeInUp(btnAddQuotes);
        Animations.fadeInUp(tblQuotes);
    }

    private void setValidations() {
        RequieredFieldsValidators.toJFXTextArea(txtDescription);
        RequieredFieldsValidators.toJFXComboBox(cmbIdCustomer);
        RequieredFieldsValidators.toJFXDatePicker(dtpDate);
    }

    private void selectText() {
        TextFieldMask.selectText(txtSearchCustomer);
        TextFieldMask.selectTextToJFXTextArea(txtDescription);
        TextFieldMask.selectText(txtPrice);
    }

    private void setMask() {
        TextFieldMask.onlyDoubleNumbers5Integers(txtPrice);
        TextFieldMask.setTextIfFieldIsEmpty(txtPrice);
    }

    @FXML
    private void showDialogAddQuotes() {
        cmbIdCustomer.setPromptText("Select a customer");
        cmbIdCustomer.setEditable(true);
        cmbIdCustomer.setDisable(false);

        resetValidations();
        enableEditControls();
        disableTable();
        rootQuotes.setEffect(Constants.BOX_BLUR_EFFECT);

        btnUpdateQuotes.setVisible(true);
        btnSaveQuotes.setDisable(false);
        containerAddQuotes.setVisible(true);
        btnSaveQuotes.toFront();
        titleWindowAddQuotes.setText("Add Quote");

        dialogAddQuote = new JFXDialogTool(containerAddQuotes, stckQuotes);
        dialogAddQuote.show();

        dialogAddQuote.setOnDialogOpened(ev -> {
            txtPrice.requestFocus();
        });

        dialogAddQuote.setOnDialogClosed(ev -> {
            containerAddQuotes.setVisible(false);
            tblQuotes.setDisable(false);
            rootQuotes.setEffect(null);
            cleanControls();
        });

        dtpDate.focusedProperty().addListener((o, oldV, newV) -> {
            if (dtpDate.getEditor().getText().isEmpty() && newV) {
                dtpDate.setValue(LocalDate.now());
            }
        });

        cmbIdCustomer.focusedProperty().addListener((o, oldV, newV) -> {
            if (!oldV) {
                cmbIdCustomer.show();
            } else {
                cmbIdCustomer.hide();
            }
        });
    }

    @FXML
    private void closeDialogAddQuotes() {
        if (dialogAddQuote != null) {
            dialogAddQuote.close();
        }
    }

    @FXML
    private void showDialogDeleteQuotes() {
        if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        containerDeleteQuotes.setVisible(true);
        rootQuotes.setEffect(Constants.BOX_BLUR_EFFECT);
        disableTable();

        dialogDeleteQuote = new JFXDialogTool(containerDeleteQuotes, stckQuotes);
        dialogDeleteQuote.show();

        dialogDeleteQuote.setOnDialogClosed(ev -> {
            containerDeleteQuotes.setVisible(false);
            tblQuotes.setDisable(false);
            rootQuotes.setEffect(null);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogDeleteQuote() {
        if (dialogDeleteQuote != null) {
            dialogDeleteQuote.close();
        }
    }

    @FXML
    private void showDialogEditQuote() {
        if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddQuotes();
        titleWindowAddQuotes.setText("Update quote");

        cmbIdCustomer.setPromptText("");
        cmbIdCustomer.setDisable(true);
        cmbIdCustomer.setEditable(true);

        selectedRecord();
        btnUpdateQuotes.toFront();
    }

    @FXML
    private void showDialogDetailsQuote() {
        if (tblQuotes.getSelectionModel().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogAddQuotes();

        titleWindowAddQuotes.setText("Quotes details");
        cmbIdCustomer.setPromptText("");
        cmbIdCustomer.setEditable(true);

        btnSaveQuotes.setDisable(true);
        btnUpdateQuotes.setVisible(false);
        btnSaveQuotes.toFront();

        selectedRecord();
        disableEditControls();
    }

    private void selectedRecord() {
        Quotes quotes = tblQuotes.getSelectionModel().getSelectedItem();
        txtPrice.setText(String.valueOf(quotes.getPrice()));
        dtpDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(quotes.getRequestDate())));
        txtDescription.setText(quotes.getDescriptionQuote());
        cmbIdCustomer.getSelectionModel().select(DatabaseHelper.searchCustomer(quotes.getCustomerName()));
        toggleButtonExists.setText(quotes.getExistence());
        toggleButtonRealized.setText(quotes.getRealization());
        toggleButtonReport.setText(quotes.getReport());

        if (quotes.getExistence().equals(Constants.EXISTENT)) {
            toggleButtonExists.selectedProperty().set(true);
        } else {
            toggleButtonExists.selectedProperty().set(false);
        }

        if (quotes.getRealization().equals(Constants.REALIZED)) {
            toggleButtonRealized.selectedProperty().set(true);
        } else {
            toggleButtonRealized.selectedProperty().set(false);
        }

        if (quotes.getReport().equals(Constants.REPORTED)) {
            toggleButtonReport.selectedProperty().set(true);
        } else {
            toggleButtonReport.selectedProperty().set(false);
        }
    }

    @FXML
    private void setActionToggleButton() {
        if (toggleButtonExists.isSelected()) {
            toggleButtonExists.setText(Constants.EXISTENT);
        } else {
            toggleButtonExists.setText(Constants.NOT_EXISTENT);
        }

        if (toggleButtonRealized.isSelected()) {
            toggleButtonRealized.setText(Constants.REALIZED);
        } else {
            toggleButtonRealized.setText(Constants.NOT_REALIZED);
        }

        if (toggleButtonReport.isSelected()) {
            toggleButtonReport.setText(Constants.REPORTED);
        } else {
            toggleButtonReport.setText(Constants.NOT_REPORTED);
        }
    }

    @FXML
    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("descriptionQuote"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colExistence.setCellValueFactory(new JFXButtonExistsCellValueFactory());
        colReport.setCellValueFactory(new JFXButtonReportCellValueFactory());
        colRealization.setCellValueFactory(new JFXButtonRealizedCellValueFactory());
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
            AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
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
        String description = txtDescription.getText().trim();

        if (dtpDate.getEditor().getText().isEmpty()) {
            dtpDate.requestFocus();
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

        if (txtPrice.getText().isEmpty()) {
            quotes.setPrice(Double.valueOf("0"));
        } else {
            quotes.setPrice(Double.valueOf(txtPrice.getText()));
        }

        if (toggleButtonExists.isSelected()) {
            quotes.setExistence(Constants.EXISTENT);
        } else {
            quotes.setExistence(Constants.NOT_EXISTENT);
        }

        if (toggleButtonRealized.isSelected()) {
            quotes.setRealization(Constants.REALIZED);
        } else {
            quotes.setRealization(Constants.NOT_REALIZED);
        }

        if (toggleButtonReport.isSelected()) {
            quotes.setReport(Constants.REPORTED);
        } else {
            quotes.setReport(Constants.NOT_REPORTED);
        }

        quotes.setDescriptionQuote(description);
        quotes.setRequestDate(java.sql.Date.valueOf(dtpDate.getValue()));
        quotes.setCustomerId(AutocompleteComboBox.getComboBoxValue(cmbIdCustomer).getId());

        boolean result = DatabaseHelper.insertNewQuote(quotes, listQuotes);
        if (result) {
            loadData();
            cleanControls();
            closeDialogAddQuotes();
            AlertsBuilder.create(AlertType.SUCCES, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_ADDED);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }
    }

    @FXML
    private void deleteQuotes() {
        boolean result = DatabaseHelper.deeleteQuotes(tblQuotes, listQuotes);
        if (result) {
            loadData();
            closeDialogDeleteQuote();
            AlertsBuilder.create(AlertType.SUCCES, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_DELETED);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }

    }

    @FXML
    private void updateQuotes() {
        String description = txtDescription.getText().trim();

        if (dtpDate.getEditor().getText().isEmpty()) {
            dtpDate.requestFocus();
            Animations.shake(dtpDate);
            return;
        }

        if (description.isEmpty()) {
            new Shake(txtDescription).play();
            return;
        }

        Quotes quotes = tblQuotes.getSelectionModel().getSelectedItem();

        if (txtPrice.getText().isEmpty()) {
            quotes.setPrice(Double.valueOf("0"));
        } else {
            quotes.setPrice(Double.valueOf(txtPrice.getText()));
        }

        if (toggleButtonExists.isSelected()) {
            quotes.setExistence(Constants.EXISTENT);
        } else {
            quotes.setExistence(Constants.NOT_EXISTENT);
        }

        if (toggleButtonRealized.isSelected()) {
            quotes.setRealization(Constants.REALIZED);
        } else {
            quotes.setRealization(Constants.NOT_REALIZED);
        }

        if (toggleButtonReport.isSelected()) {
            quotes.setReport(Constants.REPORTED);
        } else {
            quotes.setReport(Constants.NOT_REPORTED);
        }

        quotes.setDescriptionQuote(description);
        quotes.setRequestDate(java.sql.Date.valueOf(dtpDate.getValue()));
        quotes.setId(quotes.getId());

        boolean result = DatabaseHelper.updateQuotes(quotes);
        if (result) {
            loadData();
            cleanControls();
            closeDialogAddQuotes();
            AlertsBuilder.create(AlertType.SUCCES, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_UPDATED);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }
    }

    private void closeDialogWithEscapeKey() {
        rootQuotes.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogDeleteQuote();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddQuotes();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                tblQuotes.setDisable(false);
                rootQuotes.setEffect(null);
                AlertsBuilder.close();
            }

        });
    }

    private void closeDialogWithTextFields() {
        txtPrice.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddQuotes();
            }
        });

        txtDescription.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddQuotes();
            }
        });

        containerAddQuotes.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddQuotes();
            }
        });
    }

    private void deleteUserDeleteKey() {
        rootQuotes.setOnKeyPressed(ev -> {
            if (ev.getCode().equals(KeyCode.DELETE)) {
                if (tblQuotes.isDisable()) {
                    return;
                }

                if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
                    AlertsBuilder.create(AlertType.ERROR, stckQuotes, rootQuotes, tblQuotes, Constants.MESSAGE_NO_RECORD_SELECTED);
                    return;
                }
                deleteQuotes();
            }
        });
    }

    private void disableTable() {
        tblQuotes.setDisable(true);
    }

    private void cleanControls() {
        cmbIdCustomer.getSelectionModel().clearSelection();
        txtDescription.clear();
        dtpDate.setValue(null);
        txtPrice.clear();
    }

    private void disableEditControls() {
        txtDescription.setEditable(false);
        txtPrice.setEditable(false);
        dtpDate.setEditable(false);
    }

    private void enableEditControls() {
        txtDescription.setEditable(true);
        txtPrice.setEditable(true);
        dtpDate.setEditable(true);
    }

    private void resetValidations() {
        txtDescription.resetValidation();
        cmbIdCustomer.resetValidation();
        txtPrice.resetValidation();
        dtpDate.resetValidation();
    }

    @FXML
    private void filterQuotes() {
        String filterCustomers = txtSearchCustomer.getText().trim();
        if (filterCustomers.isEmpty()) {
            tblQuotes.setItems(listQuotes);
        } else {
            filterQuotes.clear();
            for (Quotes q : listQuotes) {
                if (q.getCustomerName().toLowerCase().contains(filterCustomers.toLowerCase())) {
                    filterQuotes.add(q);
                }
            }
            tblQuotes.setItems(filterQuotes);
        }
    }

    @FXML
    private void filterDescriptionQuotes() {
        String filter = txtSearchQuotes.getText().trim();
        if (filter.isEmpty()) {
            tblQuotes.setItems(listQuotes);
        } else {
            filterQuotes.clear();
            for (Quotes q : listQuotes) {
                if (q.getDescriptionQuote().toLowerCase().contains(filter.toLowerCase())) {
                    filterQuotes.add(q);
                }
            }
            tblQuotes.setItems(filterQuotes);

        }
    }

    private class JFXButtonExistsCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            JFXButton button = new JFXButton();
            button.setGraphic(icon);
            button.setText(item.getExistence());
            button.getStylesheets().add(Constants.LIGHT_THEME);
            button.setPrefWidth(colExistence.getWidth() / 0.5);

            if (item.getExistence().equals(Constants.EXISTENT)) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class JFXButtonReportCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            JFXButton button = new JFXButton();
            button.setGraphic(icon);
            button.setText(item.getReport());
            button.getStylesheets().add(Constants.LIGHT_THEME);
            button.setPrefWidth(colReport.getWidth() / 0.5);

            if (item.getReport().equals(Constants.REPORTED)) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class JFXButtonRealizedCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            JFXButton button = new JFXButton();
            button.setGraphic(icon);
            button.setText(item.getRealization());
            button.getStylesheets().add(Constants.LIGHT_THEME);
            button.setPrefWidth(colRealization.getWidth() / 0.5);

            if (item.getRealization().equals(Constants.REALIZED)) {
                icon.setIcon(FontAwesomeIcon.CHECK);
                button.getStyleClass().addAll("button-yes", "table-row-cell");
            } else {
                icon.setIcon(FontAwesomeIcon.CLOSE);
                button.getStyleClass().addAll("button-no", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }
}
