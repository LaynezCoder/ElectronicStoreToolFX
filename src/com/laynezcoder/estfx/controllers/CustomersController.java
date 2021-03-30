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

import com.laynezcoder.estfx.alerts.AlertType;
import com.laynezcoder.estfx.alerts.AlertsBuilder;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.Customers;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.constants.Constants;
import com.laynezcoder.estfx.constants.Messages;
import com.laynezcoder.estfx.constants.ResourcesPackages;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private VBox containerDelete;

    @FXML
    private VBox containerAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnSave;

    @FXML
    private TableView<Customers> tblCustomers;

    @FXML
    private TableColumn<Customers, Integer> colId;

    @FXML
    private TableColumn<Customers, String> colName;

    @FXML
    private TableColumn<Customers, String> colPhone;

    @FXML
    private TableColumn<Customers, String> colEmail;

    @FXML
    private TableColumn<Customers, String> colIt;

    @FXML
    private HBox searchContainer;

    @FXML
    private HBox tableConainer;

    @FXML
    private HBox buttonsContainer;

    @FXML
    private TextField txtSearchPhone;

    @FXML
    private TextField txtSearchName;

    @FXML
    private ImageView imageDelete;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtIt;

    @FXML
    private Text title;

    private JFXDialogTool dialogAdd;

    private JFXDialogTool dialogDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        characterLimiter();
        selectText();
        loadData();
        setMask();
        animateNodes();
        deleteUserDeleteKey();
        closeDialogWithTextFields();
        closeDialogWithEscapeKey();
        setContextMenu();
        setImages();
        filterCustomers = FXCollections.observableArrayList();
    }

    private void setImages() {
        imageDelete.setImage(ResourcesPackages.DELETE_IMAGE);
    }

    private void setContextMenu() {
        ContextMenu contextMenu = new ContextMenu(tblCustomers);

        contextMenu.setActionEdit(ev -> {
            showDialogEdit();
            contextMenu.hide();
        });

        contextMenu.setActionDelete(ev -> {
            showDialogDelete();
            contextMenu.hide();
        });

        contextMenu.setActionDetails(ev -> {
            showDialogDetails();
            contextMenu.hide();
        });

        contextMenu.setActionRefresh(ev -> {
            loadData();
            contextMenu.hide();
        });

        contextMenu.show();
    }

    private void animateNodes() {
        Animations.fadeInUp(searchContainer);
        Animations.fadeInUp(tableConainer);
    }

    private void selectText() {
        TextFieldMask.selectText(txtPhone);
        TextFieldMask.selectText(txtName);
        TextFieldMask.selectText(txtEmail);
        TextFieldMask.selectText(txtIt);
        TextFieldMask.selectText(txtSearchName);
        TextFieldMask.selectText(txtSearchPhone);
    }

    private void characterLimiter() {
        TextFieldMask.characterLimit(txtPhone, 25);
        TextFieldMask.characterLimit(txtEmail, 150);
        TextFieldMask.characterLimit(txtIt, 50);
    }

    private void setMask() {
        TextFieldMask.onlyNumbers(txtSearchPhone);
        TextFieldMask.onlyNumbers(txtPhone);
        TextFieldMask.onlyLetters(txtName, 150);
        TextFieldMask.onlyLetters(txtSearchName, 150);
    }

    @FXML
    private void showDialogdd() {
        disableTable();
        enableEditControls();

        if (!buttonsContainer.getChildren().contains(btnSave)) {
            buttonsContainer.getChildren().add(btnSave);
        }
        btnSave.setDisable(false);
        buttonsContainer.getChildren().remove(btnUpdate);

        rootCustomers.setEffect(Constants.BOX_BLUR_EFFECT);

        title.setText("Add customer");
        dialogAdd = new JFXDialogTool(containerAdd, stckCustomers);
        dialogAdd.show();

        dialogAdd.setOnDialogOpened(ev -> {
            txtName.requestFocus();
        });

        dialogAdd.setOnDialogClosed(ev -> {
            tblCustomers.setDisable(false);
            rootCustomers.setEffect(null);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogAdd() {
        if (dialogAdd != null) {
            dialogAdd.close();
        }
    }

    @FXML
    private void showDialogDelete() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Messages.NO_RECORD_SELECTED);
            return;
        }

        disableTable();
        rootCustomers.setEffect(Constants.BOX_BLUR_EFFECT);

        dialogDelete = new JFXDialogTool(containerDelete, stckCustomers);
        dialogDelete.show();

        dialogDelete.setOnDialogClosed(ev -> {
            tblCustomers.setDisable(false);
            rootCustomers.setEffect(null);
            cleanControls();
        });

    }

    @FXML
    private void closeDialogDelete() {
        if (dialogDelete != null) {
            dialogDelete.close();
        }
    }

    @FXML
    private void showDialogEdit() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Messages.NO_RECORD_SELECTED);
            return;
        }
        
        showDialogdd();
        title.setText("Update customer");
        selectedRecord();
        
        if (!buttonsContainer.getChildren().contains(btnUpdate)) {
            buttonsContainer.getChildren().add(btnUpdate);
        }
        buttonsContainer.getChildren().remove(btnSave); 
    }

    @FXML
    private void showDialogDetails() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Messages.NO_RECORD_SELECTED);
            return;
        }

        showDialogdd();
        title.setText("Customer details");
        btnSave.setDisable(true);
        disableEditControls();
        selectedRecord();

    }

    private void selectedRecord() {
        Customers customers = tblCustomers.getSelectionModel().getSelectedItem();
        txtName.setText(customers.getName());
        txtPhone.setText(customers.getPhone());
        txtEmail.setText(customers.getEmail());
        txtIt.setText(customers.getIt());
    }

    @FXML
    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("customerNumber"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("customerEmail"));
        colIt.setCellValueFactory(new PropertyValueFactory<>("it"));
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
            AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Messages.ERROR_CONNECTION_MYSQL);
        }
        listCustomers = FXCollections.observableArrayList(list);
        tblCustomers.setItems(listCustomers);
    }

    @FXML
    private void newCustomer() {
        String name = txtName.getText().trim();
        String phoneNumber = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String it = txtIt.getText().trim();

        if (name.isEmpty()) {
            txtName.requestFocus();
            Animations.shake(txtName);
            return;
        }

        if (phoneNumber.isEmpty()) {
            txtPhone.requestFocus();
            Animations.shake(txtPhone);
            return;
        }

        if (!validateEmailAddress(email) && !email.isEmpty()) {
            txtEmail.requestFocus();
            Animations.shake(txtEmail);
            NotificationsBuilder.create(NotificationType.ERROR, INVALID_EMAIL);
            return;
        }

        Customers customers = new Customers();
        customers.setName(name);
        customers.setPhone(phoneNumber);

        if (email.isEmpty()) {
            customers.setEmail(NOT_AVAILABLE);
        } else {
            customers.setEmail(email);
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
            closeDialogAdd();
            AlertsBuilder.create(AlertType.SUCCES, stckCustomers, rootCustomers, tblCustomers, Messages.ADDED_RECORD);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }

    }

    @FXML
    private void deleteCustomer() {
        boolean result = DatabaseHelper.deleteCustomer(tblCustomers, listCustomers);
        if (result) {
            loadData();
            cleanControls();
            closeDialogDelete();
            AlertsBuilder.create(AlertType.SUCCES, stckCustomers, rootCustomers, tblCustomers, Messages.DELETED_RECORD);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }
    }

    @FXML
    private void updateCustomer() {
        String name = txtName.getText().trim();
        String phoneNumber = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String it = txtIt.getText().trim();

        if (name.isEmpty()) {
            txtName.requestFocus();
            Animations.shake(txtName);
            return;
        }

        if (phoneNumber.isEmpty()) {
            txtPhone.requestFocus();
            Animations.shake(txtPhone);
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
        customers.setName(name);
        customers.setPhone(phoneNumber);

        if (email.isEmpty()) {
            customers.setEmail(NOT_AVAILABLE);
        } else {
            customers.setEmail(email);
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
            closeDialogAdd();
            AlertsBuilder.create(AlertType.SUCCES, stckCustomers, rootCustomers, tblCustomers, Messages.UPDATED_RECORD);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }
    }

    private void cleanControls() {
        txtEmail.clear();
        txtIt.clear();
        txtName.clear();
        txtPhone.clear();
    }

    private void disableEditControls() {
        txtEmail.setEditable(false);
        txtIt.setEditable(false);
        txtName.setEditable(false);
        txtPhone.setEditable(false);
    }

    private void enableEditControls() {
        txtEmail.setEditable(true);
        txtIt.setEditable(true);
        txtName.setEditable(true);
        txtPhone.setEditable(true);
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
                closeDialogDelete();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAdd();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                tblCustomers.setDisable(false);
                rootCustomers.setEffect(null);
                AlertsBuilder.close();
            }

        });
    }

    private void closeDialogWithTextFields() {
        txtName.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAdd();
            }
        });

        txtPhone.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAdd();
            }
        });

        txtEmail.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAdd();
            }
        });

        txtIt.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAdd();
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
                    AlertsBuilder.create(AlertType.ERROR, stckCustomers, rootCustomers, tblCustomers, Messages.NO_RECORD_SELECTED);
                    return;
                }
                deleteCustomer();
            }
        });
    }

    @FXML
    private void filterName() {
        String filterName = txtSearchName.getText().trim();
        if (filterName.isEmpty()) {
            tblCustomers.setItems(listCustomers);
        } else {
            filterCustomers.clear();
            for (Customers c : listCustomers) {
                if (c.getName().toLowerCase().contains(filterName.toLowerCase())) {
                    filterCustomers.add(c);
                }
            }
            tblCustomers.setItems(filterCustomers);
        }
    }

    @FXML
    private void filterPhone() {
        String filterNumber = txtSearchPhone.getText().trim();
        if (filterNumber.isEmpty()) {
            tblCustomers.setItems(listCustomers);
        } else {
            filterCustomers.clear();
            for (Customers c : listCustomers) {
                if (c.getPhone().toLowerCase().contains(filterNumber.toLowerCase())) {
                    filterCustomers.add(c);
                }
            }
            tblCustomers.setItems(filterCustomers);
        }
    }
}
