package com.laynezcoder.controllers;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.validation.RequiredFieldValidator;
import com.laynezcoder.database.DatabaseConnection;
import com.laynezcoder.database.DatabaseHelper;
import com.laynezcoder.models.Customers;
import com.laynezcoder.models.Quotes;
import com.laynezcoder.resources.Resources;
import static com.laynezcoder.resources.Resources.jfxDialog;
import com.laynezcoder.util.AutocompleteComboBox;
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
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.ESCAPE;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
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
    private TableColumn<Quotes, Integer> colCustomerId;

    @FXML
    private TableColumn<Quotes, JFXButton> colExistence;

    @FXML
    private TableColumn<Quotes, JFXButton> colRealization;

    @FXML
    private TableColumn<Quotes, JFXButton> colReport;

    @FXML
    private HBox rootSearchQuotes;

    @FXML
    private AnchorPane rootAddQuotes;

    @FXML
    private AnchorPane rootDeleteQuotes;

    @FXML
    private TextField txtSarchCodeCustomers;

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
    private JFXButton btnCancelAdd;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnCancelDelete;

    @FXML
    private JFXComboBox <Customers> cmbIdCustomer;

    @FXML
    private Text titleWindowAddQuotes;

    @FXML
    private Text titleWindowDeleteQuotes;

    @FXML
    private Text descriptionWindowDeleteQuotes;

    @FXML
    private JFXToggleButton toggleButtonExists;

    @FXML
    private JFXToggleButton toggleButtonReport;

    @FXML
    private JFXToggleButton toggleButtonRealized;

    private JFXDialog jfxDialogAddQuotes;

    private JFXDialog jfxDialogDeleteQuotes;

    private final BoxBlur blur = new BoxBlur(3, 3, 3);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filterQuotes = FXCollections.observableArrayList();
        escapeWindowWithTextFields();
        validationOfJFXDatePicker();
        setActionToggleButton();
        initializeComboBox();
        keyDeleteCustomer();
        keyEscapeWindows();
        animateNodes();
        validations();
        selectText();
        loadData();
        setFonts();
    }

    private void initializeComboBox() {
        loadComboBox();
        AutocompleteComboBox.autoCompleteComboBoxPlus(cmbIdCustomer, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.toString().equals(typedText));
    }

    private void setFonts() {
        Resources.setFontToJFXButton(btnAddQuotes, 12);
        Resources.setFontToJFXButton(btnCancelAdd, 15);
        Resources.setFontToJFXButton(btnSaveQuotes, 15);
        Resources.setFontToJFXButton(btnUpdateQuotes, 15);
        Resources.setFontToJFXButton(btnDelete, 15);
        Resources.setFontToJFXButton(btnCancelDelete, 15);

        Resources.setFontToText(titleWindowAddQuotes, 20);
        Resources.setFontToText(titleWindowDeleteQuotes, 15);
        Resources.setFontToText(descriptionWindowDeleteQuotes, 12);
    }

    private void animateNodes() {
        Resources.fadeInUpAnimation(tblQuotes);
        Resources.fadeInUpAnimation(rootSearchQuotes);
        Resources.fadeInUpAnimation(btnAddQuotes);
    }

    @FXML
    private void showWindowAddQuotes() {
        cmbIdCustomer.setPromptText("Select a customer");
        cmbIdCustomer.setEditable(true);
        cmbIdCustomer.setDisable(false);

        resetValidations();
        enableControlsEdit();
        disableTable();
        rootQuotes.setEffect(blur);

        jfxDialogAddQuotes = new JFXDialog(stckQuotes, rootAddQuotes, JFXDialog.DialogTransition.valueOf(DatabaseHelper.getDialogTransition()));
        Resources.styleAlert(jfxDialogAddQuotes);
        jfxDialogAddQuotes.setBackground(Background.EMPTY);

        rootAddQuotes.setVisible(true);
        btnUpdateQuotes.setVisible(true);
        btnSaveQuotes.setDisable(false);
        btnSaveQuotes.toFront();
        titleWindowAddQuotes.setText("Add Quote");
        jfxDialogAddQuotes.show();

        jfxDialogAddQuotes.setOnDialogOpened(ev -> {
            txtPrice.requestFocus();
        });

        jfxDialogAddQuotes.setOnDialogClosed(ev -> {
            rootAddQuotes.setVisible(false);
            tblQuotes.setDisable(false);
            rootQuotes.setEffect(null);
            cleanControls();
        });

        dtpDate.focusedProperty().addListener(ev -> {
            if (dtpDate.getEditor().getText().isEmpty()) {
                dtpDate.setValue(LocalDate.now());
            }
        });

        cmbIdCustomer.focusedProperty().addListener(ev -> {
            cmbIdCustomer.show();
        });

        Resources.setTextIsEmpty(txtPrice);
    }

    @FXML
    private void hideWindowAddQuotes() {
        jfxDialogAddQuotes.close();
    }

    @FXML
    private void showWindowDeleteQuotes() {
        if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
            Resources.simpleAlert(stckQuotes, rootQuotes, tblQuotes, "Okey", "Oops!", "Select an item from the table", "#f35f56", "#f35f56");
        } else {
            rootQuotes.setEffect(blur);
            disableTable();

            jfxDialogDeleteQuotes = new JFXDialog(stckQuotes, rootDeleteQuotes, JFXDialog.DialogTransition.valueOf(DatabaseHelper.getDialogTransition()));
            Resources.styleAlert(jfxDialogDeleteQuotes);
            jfxDialogDeleteQuotes.setBackground(Background.EMPTY);
            rootDeleteQuotes.setVisible(true);
            jfxDialogDeleteQuotes.show();

            jfxDialogDeleteQuotes.setOnDialogClosed(ev -> {
                rootDeleteQuotes.setVisible(false);
                tblQuotes.setDisable(false);
                rootQuotes.setEffect(null);
                cleanControls();
            });
        }
    }

    @FXML
    private void hideWindowDeleteQuotes() {
        jfxDialogDeleteQuotes.close();
    }

    @FXML
    private void showWindowUpdateQuotes() {
        if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
            Resources.simpleAlert(stckQuotes, rootQuotes, tblQuotes, "Okey", "Oops!", "Select an item from the table", "#f35f56", "#f35f56");
        } else {
            showWindowAddQuotes();

            titleWindowAddQuotes.setText("Update quote");

            cmbIdCustomer.setPromptText("");
            cmbIdCustomer.setDisable(true);
            cmbIdCustomer.setEditable(true);

            selectedRecord();
            btnUpdateQuotes.toFront();
        }
    }

    @FXML
    private void showWindowDetailsQuotes() {
        if (tblQuotes.getSelectionModel().isEmpty()) {
            Resources.simpleAlert(stckQuotes, rootQuotes, tblQuotes, "Okey", "Oops!", "Select an item from the table", "#f35f56", "#f35f56");
        } else {
            showWindowAddQuotes();

            titleWindowAddQuotes.setText("Quotes details");
            cmbIdCustomer.setPromptText("");
            cmbIdCustomer.setEditable(true);

            btnSaveQuotes.setDisable(true);
            btnUpdateQuotes.setVisible(false);
            btnSaveQuotes.toFront();

            selectedRecord();
            disableControlsEdit();
        }
    }

    private void selectedRecord() {
        Quotes quotes = tblQuotes.getSelectionModel().getSelectedItem();
        txtPrice.setText(String.valueOf(quotes.getPrice()));
        dtpDate.setValue(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(quotes.getRequestDate())));
        txtDescription.setText(quotes.getDescriptionQuote());
        cmbIdCustomer.getSelectionModel().select(DatabaseHelper.searchCustomer(quotes.getCustomerId()));
        toggleButtonExists.setText(quotes.getExistence());
        toggleButtonRealized.setText(quotes.getRealization());
        toggleButtonReport.setText(quotes.getReport());

        if (quotes.getExistence().equals("Existent")) {
            toggleButtonExists.selectedProperty().set(true);
        } else {
            toggleButtonExists.selectedProperty().set(false);
        }

        if (quotes.getRealization().equals("Realized")) {
            toggleButtonRealized.selectedProperty().set(true);
        } else {
            toggleButtonRealized.selectedProperty().set(false);
        }

        if (quotes.getReport().equals("Reported")) {
            toggleButtonReport.selectedProperty().set(true);
        } else {
            toggleButtonReport.selectedProperty().set(false);
        }
    }

    @FXML
    private void setActionToggleButton() {
        if (toggleButtonExists.isSelected()) {
            toggleButtonExists.setText("Existent");
        } else {
            toggleButtonExists.setText("Not existent");
        }

        if (toggleButtonRealized.isSelected()) {
            toggleButtonRealized.setText("Realized");
        } else {
            toggleButtonRealized.setText("Not realized");
        }

        if (toggleButtonReport.isSelected()) {
            toggleButtonReport.setText("Reported");
        } else {
            toggleButtonReport.setText("Not reported");
        }
    }

    @FXML
    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("descriptionQuote"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colExistence.setCellValueFactory(new JFXButtonExistsCellValueFactory());
        colReport.setCellValueFactory(new JFXButtonReportCellValueFactory());
        colRealization.setCellValueFactory(new JFXButtonRealizedCellValueFactory());
    }

    private void loadTable() {
        ArrayList<Quotes> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Quotes";
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
                int customerId = resultSet.getInt("customerId");
                list.add(new Quotes(id, descriptionQuote, requestDate, price, existence, realization, report, customerId));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        listQuotes = FXCollections.observableArrayList(list);
        tblQuotes.setItems(listQuotes);
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
        if (dtpDate.getEditor().getText().isEmpty()) {
            new Shake(dtpDate).play();
        } else if (cmbIdCustomer.getSelectionModel().isEmpty()) {
            new Shake(cmbIdCustomer).play();
        } else if (txtDescription.getText().isEmpty()) {
            new Shake(txtDescription).play();
        } else {
            Quotes quotes = new Quotes();

            if (txtPrice.getText().isEmpty()) {
                quotes.setPrice(Double.valueOf("0"));
            } else {
                quotes.setPrice(Double.valueOf(txtPrice.getText()));
            }

            if (toggleButtonExists.isSelected()) {
                quotes.setExistence("Existent");
            } else {
                quotes.setExistence("Not existent");
            }

            if (toggleButtonRealized.isSelected()) {
                quotes.setRealization("Realized");
            } else {
                quotes.setRealization("Not realized");
            }

            if (toggleButtonReport.isSelected()) {
                quotes.setReport("Reported");
            } else {
                quotes.setReport("Not reported");
            }

            quotes.setDescriptionQuote(txtDescription.getText());
            quotes.setRequestDate(java.sql.Date.valueOf(dtpDate.getValue()));
            quotes.setCustomerId(AutocompleteComboBox.getComboBoxValue(cmbIdCustomer).getId());
            DatabaseHelper.insertNewQuote(quotes, listQuotes);
            loadData();
            cleanControls();
            hideWindowAddQuotes();
            Resources.simpleAlert(stckQuotes, rootQuotes, tblQuotes, "Okey", "Nice job!", "¡Record added successfully!", "#2ab56f", "#2ab56f");
        }
    }

    @FXML
    private void deleteQuotes() {
        DatabaseHelper.deeleteQuotes(tblQuotes, listQuotes);
        loadData();
        try {
            hideWindowDeleteQuotes();
        } catch (NullPointerException ex) {
        }
        Resources.simpleAlert(stckQuotes, rootQuotes, tblQuotes, "Okey", "Nice job!", "¡Registry deleted successfully!", "#2ab56f", "#2ab56f");
    }

    @FXML
    private void updateQuotes() {
        if (dtpDate.getEditor().getText().isEmpty()) {
            new Shake(dtpDate).play();
        } else if (txtDescription.getText().isEmpty()) {
            new Shake(txtDescription).play();
        } else {
            Quotes quotes = tblQuotes.getSelectionModel().getSelectedItem();

            if (txtPrice.getText().isEmpty()) {
                quotes.setPrice(Double.valueOf("0"));
            } else {
                quotes.setPrice(Double.valueOf(txtPrice.getText()));
            }

            if (toggleButtonExists.isSelected()) {
                quotes.setExistence("Existent");
            } else {
                quotes.setExistence("Not existent");
            }

            if (toggleButtonRealized.isSelected()) {
                quotes.setRealization("Realized");
            } else {
                quotes.setRealization("Not realized");
            }

            if (toggleButtonReport.isSelected()) {
                quotes.setReport("Reported");
            } else {
                quotes.setReport("Not reported");
            }

            quotes.setDescriptionQuote(txtDescription.getText());
            quotes.setRequestDate(java.sql.Date.valueOf(dtpDate.getValue()));
            quotes.setId(quotes.getId());
            DatabaseHelper.updateQuotes(quotes);
            loadData();
            cleanControls();
            hideWindowAddQuotes();
            Resources.simpleAlert(stckQuotes, rootQuotes, tblQuotes, "Okey", "Nice job!", "¡Registry updated successfully!", "#2ab56f", "#2ab56f");
        }
    }

    private void keyEscapeWindows() {
        rootQuotes.setOnKeyReleased((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == ESCAPE && rootDeleteQuotes.isVisible()) {
                hideWindowDeleteQuotes();
            }
            if (keyEvent.getCode() == ESCAPE && rootAddQuotes.isVisible()) {
                hideWindowAddQuotes();
            }
            try {
                if (keyEvent.getCode() == ESCAPE && jfxDialog.isVisible()) {
                    tblQuotes.setDisable(false);
                    rootQuotes.setEffect(null);
                    jfxDialog.close();
                }
            } catch (NullPointerException ex) {
            }
        });
    }

    private void escapeWindowWithTextFields() {
        txtPrice.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddQuotes();
            }
        });

        txtDescription.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddQuotes();
            }
        });

        rootAddQuotes.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddQuotes();
            }
        });
    }

    private void keyDeleteCustomer() {
        rootQuotes.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                if (tblQuotes.isDisable()) {
                    System.out.println("To delete, finish saving the record or cancel the operation");
                } else if (tblQuotes.getSelectionModel().getSelectedItems().isEmpty()) {
                    Resources.simpleAlert(stckQuotes, rootQuotes, tblQuotes, "Okey", "Oops!", "Select an item from the table", "#f35f56", "#f35f56");
                } else {
                    deleteQuotes();
                }
            }
        });
    }

    private void disableTable() {
        tblQuotes.setDisable(true);
    }

    private void cleanControls() {
        txtDescription.clear();
        txtPrice.clear();
        cmbIdCustomer.getSelectionModel().clearSelection();
        dtpDate.setValue(null);
    }

    private void disableControlsEdit() {
        txtPrice.setEditable(false);
        dtpDate.setEditable(false);
        txtDescription.setEditable(false);
    }

    private void enableControlsEdit() {
        txtPrice.setEditable(true);
        dtpDate.setEditable(true);
        txtDescription.setEditable(true);
    }

    @FXML
    private void filterQuotes() {
        try {
            String filterCustomers = txtSarchCodeCustomers.getText();
            if (filterCustomers.isEmpty()) {
                tblQuotes.setItems(listQuotes);
            } else {
                filterQuotes.clear();
                for (Quotes q : listQuotes) {
                    if (q.getCustomerId() == Integer.parseInt(filterCustomers)) {
                        filterQuotes.add(q);
                    }
                }
                tblQuotes.setItems(filterQuotes);
            }
        } catch (NumberFormatException ex) {
        }
    }

    @FXML
    private void filterDescriptionQuotes() {
        String filter = txtSearchQuotes.getText();
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

    @FXML
    private void onlyNumbers() {
        Resources.validationOnlyNumbers(txtSarchCodeCustomers);
    }

    private void validationOfJFXDatePicker() {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Obligatory field");
        dtpDate.getValidators().add(validator);

        FontAwesomeIconView warnIcon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
        validator.setIcon(warnIcon);

        dtpDate.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                dtpDate.validate();
            }
        });
    }

    private void validations() {
        Resources.validationOfJFXTextArea(txtDescription);
        Resources.validationOfJFXComboBox(cmbIdCustomer);
    }

    private void resetValidations() {
        txtPrice.resetValidation();
        dtpDate.resetValidation();
        cmbIdCustomer.resetValidation();
        txtDescription.resetValidation();
    }

    private void selectText() {
        Resources.selectTextToJFXTextField(txtPrice);
        Resources.selectTextToTextField(txtSarchCodeCustomers);
        Resources.selectTextToJFXTextArea(txtDescription);

        Resources.doubleNumbersValidationTextField(txtPrice);
    }

    private class JFXButtonExistsCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            JFXButton button = new JFXButton();
            button.setText(item.getExistence());
            button.getStylesheets().add((Quotes.class.getResource(Resources.LIGHT_THEME).toExternalForm()));
            button.setPrefWidth(colExistence.getWidth() / 0.5);

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            if (item.getExistence().equals("Existent")) {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CHECK));
                button.getStyleClass().addAll("cell-button-exists", "table-row-cell");
                button.setGraphic(icon);
            } else {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CLOSE));
                button.getStyleClass().addAll("cell-button-not-exists", "table-row-cell");
                button.setGraphic(icon);
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class JFXButtonReportCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            JFXButton button = new JFXButton();
            button.setText(item.getReport());
            button.getStylesheets().add((Quotes.class.getResource(Resources.LIGHT_THEME).toExternalForm()));
            button.setPrefWidth(colReport.getWidth() / 0.5);

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            if (item.getReport().equals("Reported")) {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CHECK));
                button.getStyleClass().addAll("cell-button-exists", "table-row-cell");
                button.setGraphic(icon);
            } else {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CLOSE));
                button.getStyleClass().addAll("cell-button-not-exists", "table-row-cell");
                button.setGraphic(icon);
            }
            return new SimpleObjectProperty<>(button);
        }
    }

    private class JFXButtonRealizedCellValueFactory implements Callback<TableColumn.CellDataFeatures<Quotes, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Quotes, JFXButton> param) {
            Quotes item = param.getValue();

            JFXButton button = new JFXButton();
            button.setText(item.getRealization());
            button.getStylesheets().add((Quotes.class.getResource(Resources.LIGHT_THEME).toExternalForm()));
            button.setPrefWidth(colRealization.getWidth() / 0.5);

            FontAwesomeIconView icon = new FontAwesomeIconView();
            icon.setFill(Color.WHITE);

            if (item.getRealization().equals("Realized")) {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CHECK));
                button.getStyleClass().addAll("cell-button-exists", "table-row-cell");
                button.setGraphic(icon);
            } else {
                icon.setGlyphName(String.valueOf(FontAwesomeIcon.CLOSE));
                button.getStyleClass().addAll("cell-button-not-exists", "table-row-cell");
                button.setGraphic(icon);
            }
            return new SimpleObjectProperty<>(button);
        }
    }
}
