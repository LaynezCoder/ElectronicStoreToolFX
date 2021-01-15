package com.laynezcoder.estfx.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.estfx.alerts.AlertType;
import com.laynezcoder.estfx.alerts.AlertsBuilder;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.mask.RequieredFieldsValidators;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.Customers;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.resources.Constants;
import com.laynezcoder.estfx.util.ContextMenu;
import com.laynezcoder.estfx.util.JFXDialogTool;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
import javafx.scene.text.Text;

public class CustomersController implements Initializable {

    private static final String NOT_AVAILABLE = "N/A";

    private static final String INVALID_EMAIL = "Invalid email";

    private ObservableList<Customers> listCustomers;

    private ObservableList<Customers> filterCustomers;

    @FXML
    private StackPane stckCustomers;

    @FXML
    private AnchorPane rootCustomers;

    @FXML
    private AnchorPane containerDeleteCustomer;

    @FXML
    private AnchorPane containerAddCustomer;

    @FXML
    private JFXButton btnUpdateCustomer;

    @FXML
    private JFXButton btnSaveCustomer;

    @FXML
    private TableView<Customers> tblCustomers;

    @FXML
    private TableColumn<Customers, Integer> colCodigoCliente;

    @FXML
    private TableColumn<Customers, String> colNombreCliente;

    @FXML
    private TableColumn<Customers, String> colTelefonoCliente;

    @FXML
    private TableColumn<Customers, String> colCorreoCliente;

    @FXML
    private TableColumn<Customers, String> colNitCliente;

    @FXML
    private JFXButton btnAddCustomer;

    @FXML
    private HBox rootSearchCustomers;

    @FXML
    private TextField txtSearchNumber;

    @FXML
    private TextField txtSearchCustomer;

    @FXML
    private JFXTextField txtCustomerName;

    @FXML
    private JFXTextField txtCustomerNumber;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtIt;

    @FXML
    private Text titleAddCustomer;

    private JFXDialogTool dialogAddCustomer;

    private JFXDialogTool dialogDeleteCustomer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filterCustomers = FXCollections.observableArrayList();
        characterLimiter();
        setValidations();
        selectText();
        loadData();
        setMask();
        animateNodes();
        deleteUserDeleteKey();
        closeDialogWithTextFields();
        closeDialogWithEscapeKey();
        setContextMenu();
    }
    
    private void setContextMenu() {
        ContextMenu contextMenu = new ContextMenu(tblCustomers);

        contextMenu.setActionEdit(ev -> {
            showDialogEditCustomer();
            contextMenu.hide();
        });

        contextMenu.setActionDelete(ev -> {
            showDialogDeleteCustomer();
            contextMenu.hide();
        });

        contextMenu.setActionDetails(ev -> {
            showDialogDetailsCustomer();
            contextMenu.hide();
        });

        contextMenu.setActionRefresh(ev -> {
            loadData();
            contextMenu.hide();
        });

        contextMenu.show();
    }

    private void animateNodes() {
        Animations.fadeInUp(rootSearchCustomers);
        Animations.fadeInUp(btnAddCustomer);
        Animations.fadeInUp(tblCustomers);
    }

    private void selectText() {
        TextFieldMask.selectText(txtCustomerNumber);
        TextFieldMask.selectText(txtCustomerName);
        TextFieldMask.selectText(txtEmail);
        TextFieldMask.selectText(txtIt);
        TextFieldMask.selectText(txtSearchCustomer);
        TextFieldMask.selectText(txtSearchNumber);
    }

    private void setValidations() {
        RequieredFieldsValidators.toJFXTextField(txtCustomerNumber);
        RequieredFieldsValidators.toJFXTextField(txtCustomerName);
    }

    private void characterLimiter() {
        TextFieldMask.characterLimit(txtCustomerNumber, 25);
        TextFieldMask.characterLimit(txtEmail, 150);
        TextFieldMask.characterLimit(txtIt, 50);
    }

    private void setMask() {
        TextFieldMask.onlyNumbers(txtSearchNumber);
        TextFieldMask.onlyNumbers(txtCustomerNumber);
        TextFieldMask.onlyLetters(txtCustomerName, 150);
        TextFieldMask.onlyLetters(txtSearchCustomer, 150);
    }

    @FXML
    private void showDialogddCustomer() {
        resetValidation();
        disableTable();
        enableEditControls();
        rootCustomers.setEffect(Constants.BOX_BLUR_EFFECT);

        titleAddCustomer.setText("Add customer");
        btnUpdateCustomer.setVisible(true);
        btnSaveCustomer.setDisable(false);
        containerAddCustomer.setVisible(true);
        btnSaveCustomer.toFront();

        dialogAddCustomer = new JFXDialogTool(containerAddCustomer, stckCustomers);
        dialogAddCustomer.show();

        dialogAddCustomer.setOnDialogOpened(ev -> {
            txtCustomerName.requestFocus();
        });

        dialogAddCustomer.setOnDialogClosed(ev -> {
            containerAddCustomer.setVisible(false);
            tblCustomers.setDisable(false);
            rootCustomers.setEffect(null);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogAddCustomer() {
        if (dialogAddCustomer != null) {
            dialogAddCustomer.close();
        }
    }

    @FXML
    private void showDialogDeleteCustomer() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        disableTable();
        containerDeleteCustomer.setVisible(true);
        rootCustomers.setEffect(Constants.BOX_BLUR_EFFECT);

        dialogDeleteCustomer = new JFXDialogTool(containerDeleteCustomer, stckCustomers);
        dialogDeleteCustomer.show();

        dialogDeleteCustomer.setOnDialogClosed(ev -> {
            containerDeleteCustomer.setVisible(false);
            tblCustomers.setDisable(false);
            rootCustomers.setEffect(null);
            cleanControls();
        });

    }

    @FXML
    private void closeDialogDeleteCustomer() {
        if (dialogDeleteCustomer != null) {
            dialogDeleteCustomer.close();
        }
    }

    @FXML
    private void showDialogEditCustomer() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogddCustomer();
        titleAddCustomer.setText("Update customer");
        btnUpdateCustomer.toFront();
        selectedRecord();
    }

    @FXML
    private void showDialogDetailsCustomer() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Constants.MESSAGE_NO_RECORD_SELECTED);
            return;
        }

        showDialogddCustomer();
        titleAddCustomer.setText("Customer details");
        btnUpdateCustomer.setVisible(false);
        btnSaveCustomer.setDisable(true);
        btnSaveCustomer.toFront();
        disableEditControls();
        selectedRecord();

    }

    private void selectedRecord() {
        Customers customers = tblCustomers.getSelectionModel().getSelectedItem();
        txtCustomerName.setText(customers.getCustomerName());
        txtCustomerNumber.setText(customers.getCustomerNumber());
        txtEmail.setText(customers.getCustomerEmail());
        txtIt.setText(customers.getIt());
    }

    @FXML
    private void loadData() {
        loadTable();
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colTelefonoCliente.setCellValueFactory(new PropertyValueFactory<>("customerNumber"));
        colCorreoCliente.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
        colNitCliente.setCellValueFactory(new PropertyValueFactory<>("it"));
    }

    private void loadTable() {
        ArrayList<Customers> list = new ArrayList<>();
        try {
            String sql = "SELECT id, customerName, customerNumber, customerEmail, it FROM Customers";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String customerName = resultSet.getString("customerName");
                String customerNumber = resultSet.getString("customerNumber");
                String customerEmail = resultSet.getString("customerEmail");
                String it = resultSet.getString("it");
                list.add(new Customers(id, customerName, customerNumber, customerEmail, it));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }
        listCustomers = FXCollections.observableArrayList(list);
        tblCustomers.setItems(listCustomers);
    }

    @FXML
    private void newCustomer() {
        String name = txtCustomerName.getText().trim();
        String phoneNumber = txtCustomerNumber.getText().trim();
        String email = txtEmail.getText().trim();
        String it = txtIt.getText().trim();

        if (name.isEmpty()) {
            txtCustomerName.requestFocus();
            Animations.shake(txtCustomerName);
            return;
        }

        if (phoneNumber.isEmpty()) {
            txtCustomerNumber.requestFocus();
            Animations.shake(txtCustomerNumber);
            return;
        }

        if (!validateEmailAddress(email) && !email.isEmpty()) {
            txtEmail.requestFocus();
            Animations.shake(txtEmail);
            NotificationsBuilder.create(NotificationType.ERROR, INVALID_EMAIL);
            return;
        }

        Customers customers = new Customers();
        customers.setCustomerName(name);
        customers.setCustomerNumber(phoneNumber);

        if (email.isEmpty()) {
            customers.setCustomerEmail(NOT_AVAILABLE);
        } else {
            customers.setCustomerEmail(email);
        }

        if (it.isEmpty()) {
            customers.setIt(NOT_AVAILABLE);
        } else {
            customers.setIt(it);
        }

        boolean result = DatabaseHelper.insertNewCustomer(customers, listCustomers);
        if (result) {
            loadData();
            cleanControls();
            closeDialogAddCustomer();
            AlertsBuilder.create(AlertType.SUCCES, stckCustomers, rootCustomers, tblCustomers, Constants.MESSAGE_ADDED);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }

    }

    @FXML
    private void deleteCustomer() {
        boolean result = DatabaseHelper.deleteCustomer(tblCustomers, listCustomers);
        if (result) {
            loadData();
            cleanControls();
            closeDialogDeleteCustomer();
            AlertsBuilder.create(AlertType.SUCCES, stckCustomers, rootCustomers, tblCustomers, Constants.MESSAGE_DELETED);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }
    }

    @FXML
    private void updateCustomer() {
        String name = txtCustomerName.getText().trim();
        String phoneNumber = txtCustomerNumber.getText().trim();
        String email = txtEmail.getText().trim();
        String it = txtIt.getText().trim();

        if (name.isEmpty()) {
            txtCustomerName.requestFocus();
            Animations.shake(txtCustomerName);
            return;
        }

        if (phoneNumber.isEmpty()) {
            txtCustomerNumber.requestFocus();
            Animations.shake(txtCustomerNumber);
            return;
        }

        if (!validateEmailAddress(email) && !email.isEmpty() && !email.equals(NOT_AVAILABLE)) {
            txtEmail.requestFocus();
            Animations.shake(txtEmail);
            NotificationsBuilder.create(NotificationType.ERROR, INVALID_EMAIL);
            return;
        }

        Customers customers = tblCustomers.getSelectionModel().getSelectedItem();
        customers.setId(customers.getId());
        customers.setCustomerName(name);
        customers.setCustomerNumber(phoneNumber);

        if (email.isEmpty()) {
            customers.setCustomerEmail(NOT_AVAILABLE);
        } else {
            customers.setCustomerEmail(email);
        }

        if (it.isEmpty()) {
            customers.setIt(NOT_AVAILABLE);
        } else {
            customers.setIt(it);
        }

        boolean result = DatabaseHelper.updateCustomer(customers);
        if (result) {
            loadData();
            cleanControls();
            closeDialogAddCustomer();
            AlertsBuilder.create(AlertType.SUCCES, stckCustomers, rootCustomers, tblCustomers, Constants.MESSAGE_UPDATED);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }
    }

    private void cleanControls() {
        txtEmail.clear();
        txtIt.clear();
        txtCustomerName.clear();
        txtCustomerNumber.clear();
    }

    private void disableEditControls() {
        txtEmail.setEditable(false);
        txtIt.setEditable(false);
        txtCustomerName.setEditable(false);
        txtCustomerNumber.setEditable(false);
    }

    private void enableEditControls() {
        txtEmail.setEditable(true);
        txtIt.setEditable(true);
        txtCustomerName.setEditable(true);
        txtCustomerNumber.setEditable(true);
    }

    private void resetValidation() {
        txtCustomerNumber.resetValidation();
        txtCustomerName.resetValidation();
        txtEmail.resetValidation();
        txtIt.resetValidation();
    }

    private void disableTable() {
        tblCustomers.setDisable(true);
    }

    private boolean validateEmailAddress(String email) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    private void closeDialogWithEscapeKey() {
        rootCustomers.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogDeleteCustomer();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddCustomer();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                tblCustomers.setDisable(false);
                rootCustomers.setEffect(null);
                AlertsBuilder.close();
            }

        });
    }

    private void closeDialogWithTextFields() {
        txtCustomerName.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddCustomer();
            }
        });

        txtCustomerNumber.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddCustomer();
            }
        });

        txtEmail.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddCustomer();
            }
        });

        txtIt.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddCustomer();
            }
        });
    }

    private void deleteUserDeleteKey() {
        rootCustomers.setOnKeyPressed(ev -> {
            if (ev.getCode().equals(KeyCode.DELETE)) {
                if (tblCustomers.isDisable()) {
                    return;
                }

                if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
                    AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Constants.MESSAGE_NO_RECORD_SELECTED);
                    return;
                }
                deleteCustomer();
            }
        });
    }

    @FXML
    private void filterNameCustomer() {
        String filterName = txtSearchCustomer.getText().trim();
        if (filterName.isEmpty()) {
            tblCustomers.setItems(listCustomers);
        } else {
            filterCustomers.clear();
            for (Customers c : listCustomers) {
                if (c.getCustomerName().toLowerCase().contains(filterName.toLowerCase())) {
                    filterCustomers.add(c);
                }
            }
            tblCustomers.setItems(filterCustomers);
        }
    }

    @FXML
    private void filterNumberCustomer() {
        String filterNumber = txtSearchNumber.getText().trim();
        if (filterNumber.isEmpty()) {
            tblCustomers.setItems(listCustomers);
        } else {
            filterCustomers.clear();
            for (Customers c : listCustomers) {
                if (c.getCustomerNumber().toLowerCase().contains(filterNumber.toLowerCase())) {
                    filterCustomers.add(c);
                }
            }
            tblCustomers.setItems(filterCustomers);
        }
    }
}
