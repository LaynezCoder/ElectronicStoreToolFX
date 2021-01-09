package com.laynezcoder.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.alerts.AlertType;
import com.laynezcoder.estfx.alerts.AlertsBuilder;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.fonts.Fonts;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.models.Users;
import com.laynezcoder.estfx.resources.Constants;
import com.laynezcoder.resources.Resources;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import com.laynezcoder.util.JFXDialogTool;
import com.laynezcoder.estfx.mask.RequieredFieldsValidators;
import com.laynezcoder.estfx.mask.TextFieldMask;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javafx.scene.input.KeyCode;

public class UsersController implements Initializable {

    private final BoxBlur blur = new BoxBlur(3, 3, 3);

    @FXML
    private StackPane stckUsers;

    @FXML
    private AnchorPane rootUsers;

    @FXML
    private AnchorPane addUserContainer;

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
    private AnchorPane deleteUserContainer;

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
    private Text titleAddUser;

    @FXML
    private Text description;

    @FXML
    private FontAwesomeIconView icon;

    private JFXDialogTool dialogAddUser;

    private JFXDialogTool dialogDeleteUser;

    private ObservableList<Users> listUsers;

    private ObservableList<Users> filterUsers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showPassword();
        animateNodes();
        setValidations();
        loadData();
        setMask();
        setFonts();
        deleteUserDeleteKey();
        closeDialogWithEscapeKey();
        initalizeComboBox();
        selectTextFromTextField();
        closeDialogWithTextFields();
        filterUsers = FXCollections.observableArrayList();
    }

    private void setFonts() {
        Fonts.toButton(btnUpdateUser, 15);
        Fonts.toButton(btnNewUser, 12);
        Fonts.toButton(btnDelete, 15);
        Fonts.toButton(btnCancel, 15);
        Fonts.toButton(btnSaveUser, 15);
        Fonts.toButton(btnCancelDelete, 15);
        Fonts.toText(description, 12);
        Fonts.toText(titleAddUser, 20);
        Fonts.toText(textConfirmation, 15);
    }

    private void setValidations() {
        RequieredFieldsValidators.toJFXTextField(txtName);
        RequieredFieldsValidators.toJFXTextField(txtUser);
        RequieredFieldsValidators.toJFXTextField(txtPassword);
        RequieredFieldsValidators.toJFXPasswordField(pfPassword);
        RequieredFieldsValidators.toJFXComboBox(cmbTypeUser);
    }

    private void setMask() {
        TextFieldMask.onlyLetters(txtName, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtUser, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(pfPassword, 40);
    }

    private void selectTextFromTextField() {
        TextFieldMask.selectText(txtName);
        TextFieldMask.selectText(txtUser);
        TextFieldMask.selectText(pfPassword);
    }

    private void animateNodes() {
        Animations.fadeInUp(tblUsers);
        Animations.fadeInUp(hboxSearch);
        Animations.fadeInUp(btnNewUser);
    }

    private void initalizeComboBox() {
        cmbTypeUser.getItems().addAll("Administrator", "User");
        cmbTypeUser.focusedProperty().addListener((o, oldV, newV) -> {
            if (!oldV) {
                cmbTypeUser.show();
            } else {
                cmbTypeUser.hide();
            }
        });
    }

    @FXML
    private void showDialogAddUser() {
        disableTable();
        resetValidations();
        enableEditControls();
        rootUsers.setEffect(blur);
        btnSaveUser.toFront();
        btnUpdateUser.setVisible(true);
        btnSaveUser.setDisable(false);
        addUserContainer.setVisible(true);
        titleAddUser.setText("Add user");

        dialogAddUser = new JFXDialogTool(addUserContainer, stckUsers);
        dialogAddUser.show();

        dialogAddUser.setOnDialogOpened(ev -> {
            txtName.requestFocus();
        });

        dialogAddUser.setOnDialogClosed(ev -> {
            tblUsers.setDisable(false);
            rootUsers.setEffect(null);
            addUserContainer.setVisible(false);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogAddUser() {
        dialogAddUser.close();
    }

    @FXML
    private void showDialogDeleteUser() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, "Select an item from the table");
            return;
        }

        deleteUserContainer.setVisible(true);
        rootUsers.setEffect(blur);
        disableTable();

        dialogDeleteUser = new JFXDialogTool(deleteUserContainer, stckUsers);
        dialogDeleteUser.show();

        dialogDeleteUser.setOnDialogClosed(ev -> {
            tblUsers.setDisable(false);
            rootUsers.setEffect(null);
            cleanControls();
        });
    }

    @FXML
    private void closeDialogDeleteUser() {
        if (dialogDeleteUser != null) {
            dialogDeleteUser.close();
        }
    }

    @FXML
    private void showDialogEditUser() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, "Select an item from the table");
            return;
        }

        showDialogAddUser();
        selectedRecord();
        titleAddUser.setText("Update user");
        btnUpdateUser.toFront();
    }

    @FXML
    private void showDialogDetailsUser() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, "Select an item from the table");
            return;
        }

        showDialogAddUser();
        titleAddUser.setText("User details");
        btnSaveUser.toFront();
        btnSaveUser.setDisable(true);
        btnUpdateUser.setVisible(false);
        disableEditControls();
        selectedRecord();
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
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, "An error occurred when connecting to MySQL.\nCheck your connection to MySQL");
        }
        listUsers = FXCollections.observableArrayList(list);
        tblUsers.setItems(listUsers);
    }

    @FXML
    private void newUser() {
        String name = txtName.getText().trim();
        String user = txtUser.getText().trim();
        String password = pfPassword.getText().trim();

        if (name.isEmpty()) {
            txtName.requestFocus();
            Animations.shake(txtName);
            return;
        }

        if (user.isEmpty()) {
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        if (user.length() < 4) {
            NotificationsBuilder.create(NotificationType.ERROR, "Please enter at least 4 characters");
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        if (password.isEmpty()) {
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (password.length() < 4) {
            NotificationsBuilder.create(NotificationType.ERROR, "Please enter at least 4 characters");
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (cmbTypeUser.getSelectionModel().isEmpty()) {
            Animations.shake(cmbTypeUser);
            return;
        }

        if (DatabaseHelper.checkIfUserExists(user) != 0) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, "This user already exists");
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        Users users = new Users(name, user, password, cmbTypeUser.getSelectionModel().getSelectedItem());
        try {
            users.setProfileImage(getImage(name));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean result = DatabaseHelper.insertNewUser(users, listUsers);
        if (result) {
            closeDialogAddUser();
            loadData();
            cleanControls();
            AlertsBuilder.create(AlertType.SUCCES, stckUsers, rootUsers, tblUsers, "Record added successfully");
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, "An error occurred when connecting to MySQL.");
        }
    }

    @FXML
    private void updateUser() {
        String name = txtName.getText().trim();
        String user = txtUser.getText().trim();
        String password = txtPassword.getText().trim();
        int id = tblUsers.getSelectionModel().getSelectedItem().getId();
        String userFromTable = tblUsers.getSelectionModel().getSelectedItem().getEmail();
        String userType = tblUsers.getSelectionModel().getSelectedItem().getUserType();

        if (name.isEmpty()) {
            txtName.requestFocus();
            Animations.shake(txtName);
            return;
        }

        if (user.isEmpty()) {
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        if (user.length() < 4) {
            NotificationsBuilder.create(NotificationType.ERROR, "Please enter at least 4 characters");
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        if (password.isEmpty()) {
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (password.length() < 4) {
            NotificationsBuilder.create(NotificationType.ERROR, "Please enter at least 4 characters");
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (cmbTypeUser.getSelectionModel().isEmpty()) {
            Animations.shake(cmbTypeUser);
            return;
        }

        if (id == 1 && cmbTypeUser.getSelectionModel().getSelectedItem().equals("User")) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, "This user can only be administrator type");
            Animations.shake(cmbTypeUser);
            return;
        }

        if (!user.equals(userFromTable) && DatabaseHelper.checkIfUserExists(user) != 0) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, "This user already exists");
            Animations.shake(txtUser);
            return;
        }

        if (DatabaseHelper.getIdUserSession() == id && !cmbTypeUser.getSelectionModel().getSelectedItem().equals(userType)) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, "Unable to change user type");
            Animations.shake(cmbTypeUser);
            return;
        }

        Users users = new Users(id, name, user, password, cmbTypeUser.getSelectionModel().getSelectedItem());
        boolean result = DatabaseHelper.updateUser(users);
        if (result) {
            closeDialogAddUser();
            loadData();
            cleanControls();
            AlertsBuilder.create(AlertType.SUCCES, stckUsers, rootUsers, tblUsers, "Record updated successfully");
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, "An error occurred when connecting to MySQL.");
        }
    }

    @FXML
    private void deleteUser() {
        int id = tblUsers.getSelectionModel().getSelectedItem().getId();

        if (id == 1) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, "This user cannot be deleted");
            return;
        }

        if (id == DatabaseHelper.getIdUserSession()) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, "This user cannot be deleted");
            return;
        }

        boolean result = DatabaseHelper.deleteUser(tblUsers, listUsers);
        if (result) {
            loadData();
            closeDialogDeleteUser();
            AlertsBuilder.create(AlertType.SUCCES, stckUsers, rootUsers, tblUsers, "Record deleted successfully");
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, "UAn error occurred when connecting to MySQL.");
        }
    }

    private void showPassword() {
        txtPassword.managedProperty().bind(icon.pressedProperty());
        txtPassword.visibleProperty().bind(icon.pressedProperty());
        txtPassword.textProperty().bindBidirectional(pfPassword.textProperty());

        pfPassword.managedProperty().bind(icon.pressedProperty().not());
        pfPassword.visibleProperty().bind(icon.pressedProperty().not());

        icon.pressedProperty().addListener((o, oldVal, newVal) -> {
            if (newVal) {
                icon.setIcon(FontAwesomeIcon.EYE);
            } else {
                icon.setIcon(FontAwesomeIcon.EYE_SLASH);
            }
        });
    }

    private InputStream getImage(String name) throws FileNotFoundException {
        InputStream inputStream = null;

        char character = name.toLowerCase().charAt(0);
        switch (character) {
            case 'a':
                inputStream = getProfilePictureFromPackage("a");
                break;
            case 'b':
                inputStream = getProfilePictureFromPackage("b");
                break;
            case 'c':
                inputStream = getProfilePictureFromPackage("c");
                break;
            case 'd':
                inputStream = getProfilePictureFromPackage("d");
                break;
            case 'e':
                inputStream = getProfilePictureFromPackage("e");
                break;
            case 'f':
                inputStream = getProfilePictureFromPackage("f");
                break;
            case 'g':
                inputStream = getProfilePictureFromPackage("g");
                break;
            case 'h':
                inputStream = getProfilePictureFromPackage("h");
                break;
            case 'i':
                inputStream = getProfilePictureFromPackage("i");
                break;
            case 'j':
                inputStream = getProfilePictureFromPackage("j");
                break;
            case 'k':
                inputStream = getProfilePictureFromPackage("k");
                break;
            case 'l':
                inputStream = getProfilePictureFromPackage("l");
                break;
            case 'm':
                inputStream = getProfilePictureFromPackage("m");
                break;
            case 'n':
                inputStream = getProfilePictureFromPackage("n");
                break;
            case 'ñ':
                inputStream = getProfilePictureFromPackage("n");
                break;
            case 'o':
                inputStream = getProfilePictureFromPackage("o");
                break;
            case 'p':
                inputStream = getProfilePictureFromPackage("p");
                break;
            case 'q':
                inputStream = getProfilePictureFromPackage("q");
                break;
            case 'r':
                inputStream = getProfilePictureFromPackage("r");
                break;
            case 's':
                inputStream = getProfilePictureFromPackage("s");
                break;
            case 't':
                inputStream = getProfilePictureFromPackage("t");
                break;
            case 'u':
                inputStream = getProfilePictureFromPackage("u");
                break;
            case 'v':
                inputStream = getProfilePictureFromPackage("v");
                break;
            case 'w':
                inputStream = getProfilePictureFromPackage("w");
                break;
            case 'x':
                inputStream = getProfilePictureFromPackage("x");
                break;
            case 'y':
                inputStream = getProfilePictureFromPackage("y");
                break;
            case 'z':
                inputStream = getProfilePictureFromPackage("z");
                break;
        }
        return inputStream;
    }

    private InputStream getProfilePictureFromPackage(String imageName) throws FileNotFoundException {
        return UsersController.class.getResourceAsStream(Constants.PROFILE_PICTURES_PACKAGE + imageName + ".png");
    }

    private void selectedRecord() {
        Users users = tblUsers.getSelectionModel().getSelectedItem();
        txtName.setText(users.getNameUser());
        txtUser.setText(users.getEmail());
        pfPassword.setText(users.getPass());
        cmbTypeUser.setValue(users.getUserType());
    }

    private void disableEditControls() {
        txtName.setEditable(false);
        txtUser.setEditable(false);
        txtPassword.setEditable(false);
        pfPassword.setEditable(false);
    }

    private void enableEditControls() {
        txtName.setEditable(true);
        txtUser.setEditable(true);
        txtPassword.setEditable(true);
        pfPassword.setEditable(true);
    }

    private void cleanControls() {
        txtName.clear();
        txtUser.clear();
        txtPassword.clear();
        pfPassword.clear();
        cmbTypeUser.getSelectionModel().clearSelection();
    }

    private void resetValidations() {
        txtUser.resetValidation();
        txtName.resetValidation();
        txtPassword.resetValidation();
        pfPassword.resetValidation();
        cmbTypeUser.resetValidation();
    }

    private void disableTable() {
        tblUsers.setDisable(true);
    }

    private void closeDialogWithEscapeKey() {
        rootUsers.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogDeleteUser();
            }

            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddUser();
            }

            if (Resources.dialog != null) {
                if (ev.getCode().equals(KeyCode.ESCAPE)) {
                    tblUsers.setDisable(false);
                    rootUsers.setEffect(null);
                    Resources.dialog.close();
                }
            }
        });
        
        addUserContainer.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddUser();
            }
        });
    }

    private void closeDialogWithTextFields() {
        txtName.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddUser();
            }
        });

        txtPassword.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddUser();
            }
        });

        cmbTypeUser.setOnKeyReleased(ev -> {
            if (ev.getCode().equals(KeyCode.ESCAPE)) {
                closeDialogAddUser();
            }
        });
    }

    private void deleteUserDeleteKey() {
        rootUsers.setOnKeyPressed(ev -> {
            if (ev.getCode().equals(KeyCode.DELETE)) {
                if (tblUsers.isDisable()) {
                    return;
                }

                if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
                    Resources.showErrorAlert(stckUsers, rootUsers, tblUsers, "Select an item from the table");
                    return;
                }
                
                deleteUser();
            }
        });
    }

    @FXML
    private void filterName() {
        String name = txtSearchName.getText().trim();
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
        String user = txtSearchUser.getText().trim();
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
            password.getStylesheets().add(Constants.LIGHT_THEME);
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
            button.getStylesheets().add(Constants.LIGHT_THEME);

            if (item.getUserType().equals("Administrator")) {
                button.getStyleClass().addAll("cell-button-administrador", "table-row-cell");
            } else {
                button.getStyleClass().addAll("cell-button-user", "table-row-cell");
            }
            return new SimpleObjectProperty<>(button);
        }
    }
}
