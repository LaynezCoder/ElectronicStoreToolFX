package com.laynezcoder.controllers;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.database.DatabaseConnection;
import com.laynezcoder.database.DatabaseHelper;
import com.laynezcoder.models.Customers;
import com.laynezcoder.resources.Resources;
import static com.laynezcoder.resources.Resources.jfxDialog;
import java.net.URL;
import java.sql.Date;
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
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.ESCAPE;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class CustomersController implements Initializable {

    private ObservableList<Customers> listCustomers;

    private ObservableList<Customers> filterCustomers;

    @FXML
    private StackPane stckCustomers;

    @FXML
    private AnchorPane rootCustomers;

    @FXML
    private AnchorPane rootDeleteCustomer;

    @FXML
    private AnchorPane rootAddCustomer;

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
    private JFXButton btnCancel;
    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnCancelDelete;

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
    private Text titleWindowAddCustomer;

    @FXML
    private Text textConfirmation;

    @FXML
    private Text description;

    private JFXDialog jfxDialogAddCustomer;

    private JFXDialog jfxDialogDeleteCustomer;

    private final BoxBlur blur = new BoxBlur(3, 3, 3);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        animateNodes();
        keyEscapeWindows();
        keyDeleteCustomer();
        escapeWindowWithTextFields();
        characterLimiter();
        selectText();
        setFonts();
        validation();
        filterCustomers = FXCollections.observableArrayList();
    }

    private void setFonts() {
        Resources.setFontToJFXButton(btnAddCustomer, 12);
        Resources.setFontToJFXButton(btnCancel, 15);
        Resources.setFontToJFXButton(btnSaveCustomer, 15);
        Resources.setFontToJFXButton(btnUpdateCustomer, 15);
        Resources.setFontToJFXButton(btnCancelDelete, 15);
        Resources.setFontToJFXButton(btnDelete, 15);

        Resources.setFontToText(titleWindowAddCustomer, 20);
        Resources.setFontToText(textConfirmation, 15);
        Resources.setFontToText(description, 12);
    }

    private void animateNodes() {
        Resources.fadeInUpAnimation(tblCustomers);
        Resources.fadeInUpAnimation(rootSearchCustomers);
        Resources.fadeInUpAnimation(btnAddCustomer);
    }

    private void selectText() {
        Resources.selectTextToJFXTextField(txtCustomerName);
        Resources.selectTextToJFXTextField(txtCustomerNumber);
        Resources.selectTextToJFXTextField(txtEmail);
        Resources.selectTextToJFXTextField(txtIt);

        Resources.selectTextToTextField(txtSearchCustomer);
        Resources.selectTextToTextField(txtSearchNumber);
    }

    private void validation() {
        Resources.validationOfJFXTextField(txtCustomerName);
        Resources.validationOfJFXTextField(txtCustomerNumber);
    }

    @FXML
    private void showWindowAddCustomer() {
        resetValidation();
        enableControlsEdit();
        rootCustomers.setEffect(blur);
        disableTable();

        jfxDialogAddCustomer = new JFXDialog(stckCustomers, rootAddCustomer, JFXDialog.DialogTransition.valueOf(DatabaseHelper.getDialogTransition()));

        rootAddCustomer.setVisible(true);
        titleWindowAddCustomer.setText("Add customer");
        btnSaveCustomer.setDisable(false);
        btnUpdateCustomer.setVisible(true);
        btnSaveCustomer.toFront();

        Resources.styleAlert(jfxDialogAddCustomer);
        jfxDialogAddCustomer.setBackground(Background.EMPTY);
        jfxDialogAddCustomer.show();

        jfxDialogAddCustomer.setOnDialogOpened(ev -> {
            txtCustomerName.requestFocus();
        });

        jfxDialogAddCustomer.setOnDialogClosed(ev -> {
            tblCustomers.setDisable(false);
            rootCustomers.setEffect(null);
            rootAddCustomer.setVisible(false);
            cleanControls();
        });
    }

    @FXML
    private void hideWindowAddCustomer() {
        jfxDialogAddCustomer.close();
    }

    @FXML
    private void showWindowDeleteCustomer() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            Resources.simpleAlert(stckCustomers, rootCustomers, tblCustomers, "Okey", "Oops!", "Select an item from the table", "#f35f56", "#f35f56");
        } else {
            rootCustomers.setEffect(blur);
            disableTable();

            jfxDialogDeleteCustomer = new JFXDialog(stckCustomers, rootDeleteCustomer, JFXDialog.DialogTransition.valueOf(DatabaseHelper.getDialogTransition()));
            Resources.styleAlert(jfxDialogDeleteCustomer);
            jfxDialogDeleteCustomer.setBackground(Background.EMPTY);
            rootDeleteCustomer.setVisible(true);
            jfxDialogDeleteCustomer.show();

            jfxDialogDeleteCustomer.setOnDialogClosed(ev -> {
                tblCustomers.setDisable(false);
                rootCustomers.setEffect(null);
                rootDeleteCustomer.setVisible(false);
                cleanControls();
            });
        }
    }

    @FXML
    private void hideWindowDeleteCustomer() {
        jfxDialogDeleteCustomer.close();
    }

    @FXML
    private void showWindowUptadeCustomer() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            Resources.simpleAlert(stckCustomers, rootCustomers, tblCustomers, "Okey", "Oops!", "Select an item from the table", "#f35f56", "#f35f56");
        } else {
            showWindowAddCustomer();
            titleWindowAddCustomer.setText("Update customer");
            btnUpdateCustomer.toFront();
            selectedRecord();
        }
    }

    @FXML
    private void showWindowDetailsCustomer() {
        if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
            Resources.simpleAlert(stckCustomers, rootCustomers, tblCustomers, "Okey", "Oops!", "Select an item from the table", "#f35f56", "#f35f56");
        } else {
            showWindowAddCustomer();
            titleWindowAddCustomer.setText("Customer details");
            btnSaveCustomer.setDisable(true);
            btnUpdateCustomer.setVisible(false);
            btnSaveCustomer.toFront();
            selectedRecord();
            disableControlsEdit();
        }
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
            String sql = "SELECT * FROM Customers";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String customerName = resultSet.getString("customerName");
                String customerNumber = resultSet.getString("customerNumber");
                String customerEmail = resultSet.getString("customerEmail");
                String it = resultSet.getString("it");
                Date insertionDate = resultSet.getDate("insertionDate");
                list.add(new Customers(id, customerName, customerNumber, customerEmail, it, insertionDate));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        listCustomers = FXCollections.observableArrayList(list);
        tblCustomers.setItems(listCustomers);
    }

    @FXML
    private void newCustomer() {
        if (txtCustomerName.getText().isEmpty()) {
            new Shake(txtCustomerName).play();
        } else if (txtCustomerNumber.getText().isEmpty()) {
            new Shake(txtCustomerNumber).play();
        } else if (!validateEmailAddress(txtEmail.getText()) && !txtEmail.getText().isEmpty()) {
            new Shake(txtEmail).play();
            Resources.notification("Error", "Invalid email", "error.png");
        } else {
            Customers customers = new Customers();
            customers.setCustomerName(txtCustomerName.getText());
            customers.setCustomerNumber(txtCustomerNumber.getText());

            if (txtEmail.getText().isEmpty()) {
                customers.setCustomerEmail("N/A");
            } else {
                customers.setCustomerEmail(txtEmail.getText());
            }

            if (txtIt.getText().isEmpty()) {
                customers.setIt("N/A");
            } else {
                customers.setIt(txtIt.getText());
            }

            DatabaseHelper.insertNewCustomer(customers, listCustomers);
            loadData();
            cleanControls();
            hideWindowAddCustomer();
            Resources.simpleAlert(stckCustomers, rootCustomers, tblCustomers, "Okey", "Nice job!", "¡Registry added successfully!", "#2ab56f", "#2ab56f");
        }
    }

    @FXML
    private void deleteCustomer() {
        DatabaseHelper.deleteCustomer(tblCustomers, listCustomers);
        cleanControls();
        loadData();
        try {
            hideWindowDeleteCustomer();
        } catch (NullPointerException ex) {
        }
        Resources.simpleAlert(stckCustomers, rootCustomers, tblCustomers, "Okey", "Nice job!", "¡Registry deleted successfully!", "#2ab56f", "#2ab56f");
    }

    @FXML
    private void updateCustomer() {
        if (txtCustomerName.getText().isEmpty()) {
            new Shake(txtCustomerName).play();
        } else if (txtCustomerNumber.getText().isEmpty()) {
            new Shake(txtCustomerNumber).play();
        } else if (!validateEmailAddress(txtEmail.getText()) && !txtEmail.getText().isEmpty() && !txtEmail.getText().equals("N/A")) {
            new Shake(txtEmail).play();
            Resources.notification("Error", "Invalid email", "error.png");
        } else {
            Customers customers = tblCustomers.getSelectionModel().getSelectedItem();
            customers.setId(customers.getId());
            customers.setCustomerName(txtCustomerName.getText());
            customers.setCustomerNumber(txtCustomerNumber.getText());

            if (txtEmail.getText().isEmpty()) {
                customers.setCustomerEmail("N/A");
            } else {
                customers.setCustomerEmail(txtEmail.getText());
            }

            if (txtIt.getText().isEmpty()) {
                customers.setIt("N/A");
            } else {
                customers.setIt(txtIt.getText());
            }

            DatabaseHelper.updateCustomer(customers);
            loadData();
            cleanControls();
            hideWindowAddCustomer();
            Resources.simpleAlert(stckCustomers, rootCustomers, tblCustomers, "Okey", "Nice job!", "¡Registry updated successfully!", "#2ab56f", "#2ab56f");
        }
    }

    private void cleanControls() {
        txtCustomerName.clear();
        txtCustomerNumber.clear();
        txtEmail.clear();
        txtIt.clear();
    }

    private void disableControlsEdit() {
        txtCustomerName.setEditable(false);
        txtCustomerNumber.setEditable(false);
        txtEmail.setEditable(false);
        txtIt.setEditable(false);
    }

    private void enableControlsEdit() {
        txtCustomerName.setEditable(true);
        txtCustomerNumber.setEditable(true);
        txtEmail.setEditable(true);
        txtIt.setEditable(true);
    }

    private void keyEscapeWindows() {
        rootCustomers.setOnKeyReleased((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == ESCAPE && rootDeleteCustomer.isVisible()) {
                hideWindowDeleteCustomer();
            }
            if (keyEvent.getCode() == ESCAPE && rootAddCustomer.isVisible()) {
                hideWindowAddCustomer();
            }
            try {
                if (keyEvent.getCode() == ESCAPE && jfxDialog.isVisible()) {
                    tblCustomers.setDisable(false);
                    rootCustomers.setEffect(null);
                    jfxDialog.close();
                }
            } catch (NullPointerException ex) {
            }
        });
    }

    private void escapeWindowWithTextFields() {
        txtCustomerName.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddCustomer();
            }
        });

        txtCustomerNumber.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddCustomer();
            }
        });

        txtEmail.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddCustomer();
            }
        });

        txtIt.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddCustomer();
            }
        });
    }

    private void keyDeleteCustomer() {
        rootCustomers.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                if (tblCustomers.isDisable()) {
                    System.out.println("To delete, finish saving the record or cancel the operation");
                } else if (tblCustomers.getSelectionModel().getSelectedItems().isEmpty()) {
                    Resources.simpleAlert(stckCustomers, rootCustomers, tblCustomers, "Okey", "Oops!", "Select an item from the table", "#f35f56", "#f35f56");
                } else {
                    deleteCustomer();
                }
            }
        });
    }

    private void disableTable() {
        tblCustomers.setDisable(true);
    }

    @FXML
    private void filterNameCustomer() {
        String filterName = txtSearchCustomer.getText();
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
        String filterNumber = txtSearchNumber.getText();
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

    private void characterLimiter() {
        Resources.limitTextField(txtCustomerName, 150);
        Resources.limitTextField(txtCustomerNumber, 15);
        Resources.limitTextField(txtEmail, 150);
        Resources.limitTextField(txtIt, 50);
    }

    @FXML
    private void onlyTextFieldNumber() {
        Resources.validationOnlyNumbers(txtSearchNumber);
    }

    @FXML
    private void onlyTextFieldAddNumber() {
        Resources.validationOnlyNumbers(txtCustomerNumber);
    }

    private boolean validateEmailAddress(String email) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    private void resetValidation() {
        txtCustomerName.resetValidation();
        txtCustomerNumber.resetValidation();
        txtEmail.resetValidation();
        txtIt.resetValidation();
    }
}
