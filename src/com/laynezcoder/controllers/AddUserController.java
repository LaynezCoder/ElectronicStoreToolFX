package com.laynezcoder.controllers;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.database.DatabaseConnection;
import com.laynezcoder.database.DatabaseHelper;
import com.laynezcoder.models.Users;
import com.laynezcoder.resources.Resources;
import static com.laynezcoder.resources.Resources.jfxDialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.net.URL;
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
import javafx.scene.control.PasswordField;
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

public class AddUserController implements Initializable {

    private ObservableList<Users> listUsers;

    private ObservableList<Users> filterUsers;

    @FXML
    private StackPane stckUsers;

    @FXML
    private AnchorPane rootUsers;

    @FXML
    private AnchorPane rootAddUser;

    @FXML
    private HBox hboxSearch;

    @FXML
    private TableView<Users> tblUsers;

    @FXML
    private TableColumn<Users, Integer> colId;

    @FXML
    private TableColumn<Users, String> colName;

    @FXML
    private TableColumn<Users, String> colUser;

    @FXML
    private TableColumn<Users, PasswordField> colPassword;

    @FXML
    private TableColumn<Users, JFXButton> colTypeUser;

    @FXML
    private TextField txtSearchName;

    @FXML
    private TextField txtSearchUser;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXPasswordField pfPassword;

    @FXML
    private JFXComboBox<String> cmbTypeUser;

    @FXML
    private JFXButton btnNewUser;

    @FXML
    private AnchorPane rootDeleteUser;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnCancelDelete;

    @FXML
    private JFXButton btnSaveUser;

    @FXML
    private JFXButton btnUpdateUser;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private Text textConfirmation;

    @FXML
    private Text titleWindowAddUser;

    @FXML
    private Text description;

    @FXML
    private FontAwesomeIconView icon;

    private final BoxBlur blur = new BoxBlur(3, 3, 3);

    private JFXDialog dialogAddUser;

    private JFXDialog dialogDeleteUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filterUsers = FXCollections.observableArrayList();
        escapeWindowWithTextFields();
        setOptionsToComboBox();
        keyDeleteCustomer();
        maxinumCharactert();
        keyEscapeWindows();
        showPassword();
        animateNodes();
        validations();
        selectText();
        loadData();
        setFonts();
    }

    private void setFonts() {
        Resources.setFontToJFXButton(btnCancelDelete, 15);
        Resources.setFontToJFXButton(btnUpdateUser, 15);
        Resources.setFontToJFXButton(btnSaveUser, 15);
        Resources.setFontToJFXButton(btnNewUser, 12);
        Resources.setFontToJFXButton(btnCancel, 15);
        Resources.setFontToJFXButton(btnDelete, 15);

        Resources.setFontToText(titleWindowAddUser, 20);
        Resources.setFontToText(textConfirmation, 15);
        Resources.setFontToText(description, 12);
    }

    private void validations() {
        Resources.validationOfJFXTextField(txtName);
        Resources.validationOfJFXTextField(txtUser);
        Resources.validationOfJFXTextField(txtPassword);
        Resources.validationOfJFXPasswordField(pfPassword);
        Resources.validationOfJFXComboBox(cmbTypeUser);
    }

    private void selectText() {
        Resources.selectTextToJFXTextField(txtName);
        Resources.selectTextToJFXTextField(txtUser);
        Resources.selectTextToJFXPasswordField(pfPassword);
    }

    private void setOptionsToComboBox() {
        cmbTypeUser.getItems().addAll("Administrator", "User");
    }

    private void animateNodes() {
        Resources.fadeInUpAnimation(tblUsers);
        Resources.fadeInUpAnimation(hboxSearch);
        Resources.fadeInUpAnimation(btnNewUser);
    }

    @FXML
    private void showWindowAddUser() {
        resetValidation();
        enableControlsEdit();
        rootUsers.setEffect(blur);

        titleWindowAddUser.setText("Add user");
        btnSaveUser.setDisable(false);
        btnUpdateUser.setVisible(true);
        btnSaveUser.toFront();
        
        dialogAddUser = new JFXDialog();
        dialogAddUser.setTransitionType(JFXDialog.DialogTransition.valueOf(DatabaseHelper.getDialogTransition()));
        dialogAddUser.setBackground(Background.EMPTY);
        dialogAddUser.setDialogContainer(stckUsers);
        dialogAddUser.setContent(rootAddUser);
        Resources.styleAlert(dialogAddUser);
        rootAddUser.setVisible(true);      
        dialogAddUser.show();

        dialogAddUser.setOnDialogOpened(ev -> {
            txtName.requestFocus();
        });

        dialogAddUser.setOnDialogClosed(ev -> {
            tblUsers.setDisable(false);
            rootUsers.setEffect(null);
            rootAddUser.setVisible(false);
            cleanControls();
        });
    }

    @FXML
    private void hideWindowAddUser() {
        dialogAddUser.close();
    }

    @FXML
    private void showWindowDeleteUser() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            Resources.showErrorAlert(stckUsers, rootUsers, tblUsers, "Select an item from the table");
        } else {
            rootUsers.setEffect(blur);
            disableTable();
            
            dialogDeleteUser = new JFXDialog();
            dialogDeleteUser.setTransitionType(JFXDialog.DialogTransition.valueOf(DatabaseHelper.getDialogTransition()));
            dialogDeleteUser.setBackground(Background.EMPTY);
            dialogDeleteUser.setDialogContainer(stckUsers);
            dialogDeleteUser.setContent(rootDeleteUser);
            Resources.styleAlert(dialogDeleteUser);
            rootDeleteUser.setVisible(true);
            dialogDeleteUser.show();

            cmbTypeUser.focusedProperty().addListener(ev -> {
                cmbTypeUser.show();
            });

            dialogDeleteUser.setOnDialogClosed(ev -> {
                tblUsers.setDisable(false);
                rootUsers.setEffect(null);
                rootDeleteUser.setVisible(false);
                cleanControls();
            });
        }
    }

    @FXML
    private void hideWindowDeleteUser() {
        try {
            dialogDeleteUser.close();
        } catch (NullPointerException ex) {}
    }

    @FXML
    private void showWindowUptadeProduct() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            Resources.showErrorAlert(stckUsers, rootUsers, tblUsers, "Select an item from the table");
        } else {
            showWindowAddUser();
            titleWindowAddUser.setText("Update user");
            btnUpdateUser.toFront();
            selectedRecord();
        }
    }

    @FXML
    private void showWindowDetailsProduct() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            Resources.showErrorAlert(stckUsers, rootUsers, tblUsers, "Select an item from the table");
        } else {
            showWindowAddUser();
            titleWindowAddUser.setText("User details");
            btnUpdateUser.setVisible(false);
            btnSaveUser.setDisable(true);
            btnSaveUser.toFront();
            disableControlsEdit();
            selectedRecord();
        }
    }

    private void disableTable() {
        tblUsers.setDisable(true);
    }

    @FXML
    private void loadData() {
        laodTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nameUser"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new JFXPasswordCellValueFactory());
        colTypeUser.setCellValueFactory(new JFXButtonTypeUserCellValueFactory());
    }

    private void laodTable() {
        ArrayList<Users> list = new ArrayList<>();
        try {
            String sql = "SELECT id, nameUser, email, pass, userType FROM Users";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nameUser = resultSet.getString("nameUser");
                String email = resultSet.getString("email");
                String pass = resultSet.getString("pass");
                String userType = resultSet.getString("userType");
                list.add(new Users(id, nameUser, email, pass, userType));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddUserController.class.getName()).log(Level.SEVERE, null, ex);
            Resources.showErrorAlert(stckUsers, rootUsers, tblUsers, "An error occurred when connecting to MySQL.\n"
                    + "Check your connection to MySQL");
        }
        listUsers = FXCollections.observableArrayList(list);
        tblUsers.setItems(listUsers);
    }

    @FXML
    private void newUser() {
        if (txtName.getText().isEmpty()) {
            new Shake(txtName).play();
        } else if (txtUser.getText().isEmpty()) {
            new Shake(txtUser).play();
        } else if (txtUser.getText().length() < 4) {
            Resources.notification("Error", "Please enter at least 4 characters", "error.png");
            new Shake(txtUser).play();
        } else if (pfPassword.getText().isEmpty()) {
            new Shake(pfPassword).play();
        } else if (txtPassword.getText().length() < 4) {
            Resources.notification("Error", "Please enter at least 4 characters", "error.png");
            new Shake(pfPassword).play();
        } else if (cmbTypeUser.getSelectionModel().isEmpty()) {
            new Shake(cmbTypeUser).play();
        } else if (DatabaseHelper.checkIfUserExists(txtUser.getText()) != 0) {
            Resources.notification("Invalid action", "This user already exists", "error.png");
            new Shake(txtUser).play();
        } else {
            Users users = new Users(txtName.getText(), txtUser.getText(), pfPassword.getText(), cmbTypeUser.getSelectionModel().getSelectedItem());
            boolean result = DatabaseHelper.insertNewUser(users, listUsers);
            if (result) {
                loadData();
                cleanControls();
                hideWindowAddUser();
                Resources.showSuccessAlert(stckUsers, rootUsers, tblUsers, "Registry added successfully");
            } else {
                Resources.notification("FATAL ERROR", "An error occurred when connecting to MySQL.", "error.png");
            }
        }
    }

    @FXML
    private void updateUser() {
        int id = tblUsers.getSelectionModel().getSelectedItem().getId();
        String user = tblUsers.getSelectionModel().getSelectedItem().getEmail();
        String userType = tblUsers.getSelectionModel().getSelectedItem().getUserType();

        if (txtName.getText().isEmpty()) {
            new Shake(txtName).play();
        } else if (txtUser.getText().isEmpty()) {
            new Shake(txtUser).play();
        } else if (txtUser.getText().length() < 4) {
            Resources.notification("Error", "Please enter at least 4 characters", "error.png");
            new Shake(txtUser).play();
        } else if (pfPassword.getText().isEmpty()) {
            new Shake(pfPassword).play();
        } else if (txtPassword.getText().length() < 4) {
            Resources.notification("Error", "Please enter at least 4 characters", "error.png");
            new Shake(pfPassword).play();
        } else if (cmbTypeUser.getSelectionModel().isEmpty()) {
            new Shake(cmbTypeUser).play();
        } else if (id == 1 && cmbTypeUser.getSelectionModel().getSelectedItem().equals("User")) {
            new Shake(cmbTypeUser).play();
            Resources.notification("Invalid action", "This user can only be administrator type", "error.png");
        } else if (!txtUser.getText().equals(user) && DatabaseHelper.checkIfUserExists(txtUser.getText()) != 0) {
            new Shake(txtUser).play();
            Resources.notification("Invalid action", "This user already exists", "error.png");
        } else if (DatabaseHelper.getIdUserSession() == id && !cmbTypeUser.getSelectionModel().getSelectedItem().equals(userType)) {
            Resources.notification("Invalid action", "Unable to change user type", "error.png");
            new Shake(cmbTypeUser).play();
        } else {
            Users users = new Users(id, txtName.getText(), txtUser.getText(), pfPassword.getText(), cmbTypeUser.getSelectionModel().getSelectedItem());
            boolean result = DatabaseHelper.updateUser(users);
            if (result) {
                loadData();
                cleanControls();
                hideWindowAddUser();
                Resources.showSuccessAlert(stckUsers, rootUsers, tblUsers, "Registry updated successfully");
            } else {
                Resources.notification("FATAL ERROR", "An error occurred when connecting to MySQL.", "error.png");
            }
        }
    }

    @FXML
    private void deleteUser() {
        int id = tblUsers.getSelectionModel().getSelectedItem().getId();

        if (id == 1) {
            Resources.notification("Invalid action", "This user cannot be deleted", "error.png");
        } else if (id == DatabaseHelper.getIdUserSession()) {
            Resources.notification("Invalid action", "This user cannot be deleted", "error.png");
        } else {
            boolean result = DatabaseHelper.deleteUser(tblUsers, listUsers);
            if (result) {
                loadData();
                hideWindowDeleteUser();
                Resources.showSuccessAlert(stckUsers, rootUsers, tblUsers, "Registry deleted successfully");
            } else {
                Resources.notification("FATAL ERROR", "An error occurred when connecting to MySQL.", "error.png");
            }
            
        }
    }

    @FXML
    private void showPassword() {
        txtPassword.managedProperty().bind(icon.pressedProperty());
        txtPassword.visibleProperty().bind(icon.pressedProperty());

        pfPassword.managedProperty().bind(icon.pressedProperty().not());
        pfPassword.visibleProperty().bind(icon.pressedProperty().not());

        txtPassword.textProperty().bindBidirectional(pfPassword.textProperty());
        
        icon.pressedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal) {
                icon.setIcon(FontAwesomeIcon.EYE);
            } else {
                icon.setIcon(FontAwesomeIcon.EYE_SLASH);
            }
        });
    }

    private void maxinumCharactert() {
        Resources.limitTextField(txtName, 150);
        Resources.limitTextField(txtUser, 150);
        Resources.limitTextField(txtPassword, 150);
        Resources.limitTextField(pfPassword, 150);
    }

    private void selectedRecord() {
        Users users = tblUsers.getSelectionModel().getSelectedItem();
        txtName.setText(users.getNameUser());
        txtUser.setText(users.getEmail());
        pfPassword.setText(users.getPass());
        cmbTypeUser.setValue(String.valueOf(users.getUserType()));
    }

    private void disableControlsEdit() {
        txtName.setEditable(false);
        txtUser.setEditable(false);
        txtPassword.setEditable(false);
        pfPassword.setEditable(false);
    }

    private void cleanControls() {
        txtName.clear();
        txtUser.clear();
        txtPassword.clear();
        pfPassword.clear();
        cmbTypeUser.getSelectionModel().clearSelection();
    }

    private void enableControlsEdit() {
        txtName.setEditable(true);
        txtUser.setEditable(true);
        txtPassword.setEditable(true);
        pfPassword.setEditable(true);
    }

    private void resetValidation() {
        txtName.resetValidation();
        txtPassword.resetValidation();
        pfPassword.resetValidation();
        txtUser.resetValidation();
        cmbTypeUser.resetValidation();
    }

    private void keyEscapeWindows() {
        rootUsers.setOnKeyReleased((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == ESCAPE && rootDeleteUser.isVisible()) {
                hideWindowDeleteUser();
            }
            if (keyEvent.getCode() == ESCAPE && rootAddUser.isVisible()) {
                hideWindowAddUser();
            }
            try {
                if (keyEvent.getCode() == ESCAPE && jfxDialog.isVisible()) {
                    tblUsers.setDisable(false);
                    rootUsers.setEffect(null);
                    jfxDialog.close();
                }
            } catch (NullPointerException ex) {}
        });
    }

    private void escapeWindowWithTextFields() {
        txtName.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddUser();
            }
        });

        txtPassword.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddUser();
            }
        });

        cmbTypeUser.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddUser();
            }
        });

        rootAddUser.setOnKeyReleased(ev -> {
            if (ev.getCode() == ESCAPE) {
                hideWindowAddUser();
            }
        });
    }

    private void keyDeleteCustomer() {
        rootUsers.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                if (tblUsers.isDisable()) {
                    System.out.println("To delete, finish saving the record or cancel the operation");
                } else if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
                    Resources.showErrorAlert(stckUsers, rootUsers, tblUsers, "Select an item from the table");
                } else {
                    deleteUser();
                }
            }
        });
    }

    @FXML
    private void filterName() {
        String name = txtSearchName.getText();
        if (name.isEmpty()) {
            tblUsers.setItems(listUsers);
        } else {
            filterUsers.clear();
            for (Users u : listUsers) {
                if (u.getNameUser().toLowerCase().contains(name.toLowerCase())) {
                    filterUsers.add(u);
                }
            }
            tblUsers.setItems(filterUsers);
        }
    }

    @FXML
    private void filterUser() {
        String user = txtSearchUser.getText();
        if (user.isEmpty()) {
            tblUsers.setItems(listUsers);
        } else {
            filterUsers.clear();
            for (Users u : listUsers) {
                if (u.getEmail().toLowerCase().contains(user.toLowerCase())) {
                    filterUsers.add(u);
                }
            }
            tblUsers.setItems(filterUsers);
        }
    }

    private class JFXPasswordCellValueFactory implements Callback<TableColumn.CellDataFeatures<Users, PasswordField>, ObservableValue<PasswordField>> {

        @Override
        public ObservableValue<PasswordField> call(TableColumn.CellDataFeatures<Users, PasswordField> param) {
            Users item = param.getValue();

            PasswordField password = new PasswordField();
            password.setEditable(false);
            password.setPrefWidth(colPassword.getWidth() / 0.5);
            password.setText(item.getPass());
            password.getStylesheets().add((AddUserController.class.getResource(Resources.LIGHT_THEME).toExternalForm()));
            password.getStyleClass().addAll("password-field-cell", "table-row-cell");

            return new SimpleObjectProperty<>(password);
        }
    }

    private class JFXButtonTypeUserCellValueFactory implements Callback<TableColumn.CellDataFeatures<Users, JFXButton>, ObservableValue<JFXButton>> {

        @Override
        public ObservableValue<JFXButton> call(TableColumn.CellDataFeatures<Users, JFXButton> param) {
            Users item = param.getValue();

            JFXButton button = new JFXButton();
            button.setPrefWidth(colTypeUser.getWidth() / 0.5);
            button.setText(item.getUserType());
            button.getStylesheets().add((AddUserController.class.getResource(Resources.LIGHT_THEME).toExternalForm()));

            if (item.getUserType().equals("Administrator")) {
                button.getStyleClass().addAll("cell-button-administrador", "table-row-cell");
            } else {
                button.getStyleClass().addAll("cell-button-user", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }
}
