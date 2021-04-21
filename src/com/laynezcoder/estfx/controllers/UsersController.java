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

import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.alerts.AlertType;
import com.laynezcoder.estfx.alerts.AlertsBuilder;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.models.Users;
import com.laynezcoder.estfx.constants.Constants;
import com.laynezcoder.estfx.constants.Messages;
import com.laynezcoder.estfx.constants.Views;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import com.laynezcoder.estfx.util.JFXDialogTool;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.UserSession;
import com.laynezcoder.estfx.util.ContextMenu;
import com.laynezcoder.estfx.util.EstfxUtil;
import com.laynezcoder.estfx.util.I18NUtil;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UsersController implements Initializable {

    private final String CANNOT_DELETED = "This user cannot be deleted";

    private final String ADMINISTRATOR_ONLY = "This user can only be administrator type";

    private final String UNABLE_TO_CHANGE = "Unable to change user type";

    @FXML
    private StackPane stckUsers;

    @FXML
    private AnchorPane rootUsers;

    @FXML
    private VBox containerAdd;

    @FXML
    private HBox hboxSearch;

    @FXML
    private HBox buttonsContainer;

    @FXML
    private TableView<Users> tblUsers;

    @FXML
    private TableColumn<Users, Integer> colId;

    @FXML
    private TableColumn<Users, HBox> colUsername;

    @FXML
    private TableColumn<Users, PasswordField> colPassword;

    @FXML
    private TableColumn<Users, Button> colAction;

    @FXML
    private TextField txtSearchUsername;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtPassword;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private ComboBox<String> cmbTypeUser;

    @FXML
    private AnchorPane deleteUserContainer;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Text title;

    @FXML
    private FontAwesomeIconView icon;

    private JFXDialogTool dialogAddUser;

    private JFXDialogTool dialogDeleteUser;

    private ContextMenu contextMenu;

    private ObservableList<Users> listUsers;

    private ObservableList<Users> filterUsers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showPassword();
        animateNodes();
        loadData();
        setMask();
        setContextMenu();
        deleteUserDeleteKey();
        initalizeComboBox();
        selectText();
        filterUsers = FXCollections.observableArrayList();
    }

    private void setContextMenu() {
        contextMenu = new ContextMenu(tblUsers);

        contextMenu.setActionEdit(ev -> {
            showDialogEditUser();
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

        contextMenu.display();
    }

    private void setMask() {
        TextFieldMask.onlyLetters(txtName, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtUser, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtSearchUsername, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(pfPassword, 40);
    }

    private void selectText() {
        TextFieldMask.selectText(txtName);
        TextFieldMask.selectText(txtUser);
        TextFieldMask.selectText(pfPassword);
    }

    private void animateNodes() {
        Animations.fadeInUp(tblUsers);
        Animations.fadeInUp(hboxSearch);
    }

    private void initalizeComboBox() {
        cmbTypeUser.getItems().addAll("Administrator", "User");
        cmbTypeUser.focusedProperty().addListener((o, oldValue, newValue) -> {
            if (newValue) {
                cmbTypeUser.show();
            }
        });
    }

    @FXML
    private void showDialogAdd() {
        enableEditControls();
        tblUsers.setDisable(true);
        title.setText("Add user");
        rootUsers.setEffect(Constants.BOX_BLUR_EFFECT);

        if (!buttonsContainer.getChildren().contains(btnSave)) {
            buttonsContainer.getChildren().add(btnSave);
        }
        btnSave.setDisable(false);
        buttonsContainer.getChildren().remove(btnUpdate);

        dialogAddUser = new JFXDialogTool(containerAdd, stckUsers);
        dialogAddUser.show();

        dialogAddUser.setOnDialogOpened(ev -> {
            txtName.requestFocus();
        });

        dialogAddUser.setOnDialogClosed(ev -> {
            if (!AlertsBuilder.isVisible()) {
                tblUsers.setDisable(false);
                rootUsers.setEffect(null);
                cleanControls();
            }
        });
    }

    @FXML
    private void closeDialogAdd() {
        dialogAddUser.close();
    }

    private void showDialogDelete() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, Messages.NO_RECORD_SELECTED);
            return;
        }

        tblUsers.setDisable(true);
        rootUsers.setEffect(Constants.BOX_BLUR_EFFECT);

        dialogDeleteUser = new JFXDialogTool(deleteUserContainer, stckUsers);
        dialogDeleteUser.show();

        dialogDeleteUser.setOnDialogClosed(ev -> {
            if (!AlertsBuilder.isVisible()) {
                tblUsers.setDisable(false);
                rootUsers.setEffect(null);
            }
        });
    }

    @FXML
    private void closeDialogDelete() {
        if (dialogDeleteUser != null) {
            dialogDeleteUser.close();
        }
    }

    @FXML
    private void showDialogEditUser() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, Messages.NO_RECORD_SELECTED);
            return;
        }

        showDialogAdd();
        selectedRecord();
        title.setText("Update user");

        if (!buttonsContainer.getChildren().contains(btnUpdate)) {
            buttonsContainer.getChildren().add(btnUpdate);
        }
        buttonsContainer.getChildren().remove(btnSave);
    }

    @FXML
    private void showDialogDetails() {
        if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, Messages.NO_RECORD_SELECTED);
            return;
        }

        showDialogAdd();
        title.setText("User details");
        btnSave.setDisable(true);
        disableEditControls();
        selectedRecord();
    }

    @FXML
    private void loadData() {
        laodTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsername.setCellValueFactory(new UsernameCellValueFactory());
        colAction.setCellValueFactory(new ActionCellValueFactory());
        colPassword.setCellValueFactory(new PasswordCellValueFactory());
    }

    private void laodTable() {
        ArrayList<Users> list = new ArrayList<>();
        try {
            String sql = "SELECT id, fullname, username, pass, userType FROM Users";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fullname = resultSet.getString("fullname");
                String username = resultSet.getString("username");
                String pass = resultSet.getString("pass");
                String userType = resultSet.getString("userType");
                list.add(new Users(id, fullname, username, pass, userType));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, Messages.ERROR_CONNECTION_MYSQL);
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

        if (user.length() < 5) {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ENTER_AT_LEAST_5_CHARACTERES);
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        if (password.isEmpty()) {
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (password.length() < 5) {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ENTER_AT_LEAST_5_CHARACTERES);
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (cmbTypeUser.getSelectionModel().isEmpty()) {
            Animations.shake(cmbTypeUser);
            return;
        }

        if (DatabaseHelper.checkIfUserExists(user) != 0) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, Messages.USER_ALREADY_EXISTS);
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        Users users = new Users(name, user, password, cmbTypeUser.getSelectionModel().getSelectedItem());
        boolean result = DatabaseHelper.insertNewUser(users);
        if (result) {
            closeDialogAdd();
            loadData();
            cleanControls();
            AlertsBuilder.create(AlertType.SUCCES, stckUsers, rootUsers, tblUsers, Messages.ADDED_RECORD);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }
    }

    @FXML
    private void updateUser() {
        String name = txtName.getText().trim();
        String user = txtUser.getText().trim();
        String password = txtPassword.getText().trim();
        int id = tblUsers.getSelectionModel().getSelectedItem().getId();
        String userFromTable = tblUsers.getSelectionModel().getSelectedItem().getUsername();

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

        if (user.length() < 5) {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ENTER_AT_LEAST_5_CHARACTERES);
            txtUser.requestFocus();
            Animations.shake(txtUser);
            return;
        }

        if (password.isEmpty()) {
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (password.length() < 5) {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ENTER_AT_LEAST_5_CHARACTERES);
            pfPassword.requestFocus();
            Animations.shake(pfPassword);
            return;
        }

        if (cmbTypeUser.getSelectionModel().isEmpty()) {
            Animations.shake(cmbTypeUser);
            return;
        }

        if (id == 1 && cmbTypeUser.getSelectionModel().getSelectedIndex() == 1) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, ADMINISTRATOR_ONLY);
            Animations.shake(cmbTypeUser);
            return;
        }

        if (!user.equals(userFromTable) && DatabaseHelper.checkIfUserExists(user) != 0) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, Messages.USER_ALREADY_EXISTS);
            Animations.shake(txtUser);
            return;
        }

        if (UserSession.getInstace().getId() == id && cmbTypeUser.getSelectionModel().getSelectedIndex() == 1) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, UNABLE_TO_CHANGE);
            Animations.shake(cmbTypeUser);
            return;
        }

        Users users = new Users(id, name, user, password, cmbTypeUser.getSelectionModel().getSelectedItem());
        boolean result = DatabaseHelper.updateUser(users);
        if (result) {
            closeDialogAdd();
            loadData();
            cleanControls();
            AlertsBuilder.create(AlertType.SUCCES, stckUsers, rootUsers, tblUsers, Messages.UPDATED_RECORD);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
        }
    }

    @FXML
    private void deleteUser() {
        int id = tblUsers.getSelectionModel().getSelectedItem().getId();

        if (id == 1) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, CANNOT_DELETED);
            return;
        }

        if (id == UserSession.getInstace().getId()) {
            NotificationsBuilder.create(NotificationType.INVALID_ACTION, CANNOT_DELETED);
            return;
        }

        boolean result = DatabaseHelper.deleteUser(tblUsers);
        if (result) {
            loadData();
            closeDialogDelete();
            AlertsBuilder.create(AlertType.SUCCES, stckUsers, rootUsers, tblUsers, Messages.DELETED_RECORD);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Messages.ERROR_CONNECTION_MYSQL);
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
                txtPassword.requestFocus();
                icon.setIcon(FontAwesomeIcon.EYE);
            } else {
                pfPassword.requestFocus();
                icon.setIcon(FontAwesomeIcon.EYE_SLASH);
            }
        });
    }

    private void selectedRecord() {
        Users users = tblUsers.getSelectionModel().getSelectedItem();
        txtName.setText(users.getName());
        txtUser.setText(users.getUsername());
        pfPassword.setText(users.getPassword());
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

    private void deleteUserDeleteKey() {
        rootUsers.setOnKeyPressed(ev -> {
            if (ev.getCode().equals(KeyCode.DELETE)) {
                if (contextMenu.isShowing()) {
                    contextMenu.hide();
                }

                if (tblUsers.isDisable()) {
                    return;
                }

                if (tblUsers.getSelectionModel().getSelectedItems().isEmpty()) {
                    AlertsBuilder.create(AlertType.ERROR, stckUsers, rootUsers, tblUsers, Messages.NO_RECORD_SELECTED);
                    return;
                }

                deleteUser();
            }
        });
    }

    private void loadProfile(int id) {
        try {
            FXMLLoader loader = I18NUtil.FXMLLoader(Views.SETTINGS);

            Parent root = loader.load();
            SettingsController profile = loader.getController();

            profile.init(id);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setMinHeight(Constants.MIN_HEIGHT);
            stage.setMinWidth(Constants.MIN_WIDTH);
            stage.getIcons().add(Constants.ICON);
            stage.setTitle(Constants.TITLE);
            stage.setScene(new Scene(root));
            stage.initOwner(getStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Stage getStage() {
        return (Stage) txtName.getScene().getWindow();
    }

    @FXML
    private void filterUsername() {
        String user = txtSearchUsername.getText().trim();
        if (user.isEmpty()) {
            tblUsers.setItems(listUsers);
        } else {
            filterUsers.clear();
            for (Users u : listUsers) {
                if (u.getUsername().toLowerCase().contains(user.toLowerCase())) {
                    filterUsers.add(u);
                }
            }
            tblUsers.setItems(filterUsers);
        }
    }

    private class PasswordCellValueFactory implements Callback<TableColumn.CellDataFeatures<Users, PasswordField>, ObservableValue<PasswordField>> {

        @Override
        public ObservableValue<PasswordField> call(TableColumn.CellDataFeatures<Users, PasswordField> param) {
            Users item = param.getValue();

            PasswordField password = new PasswordField();
            password.setDisable(true);
            password.setText(item.getPassword());
            password.getStyleClass().add("password-field-cell");

            return new SimpleObjectProperty<>(password);
        }
    }

    private class UsernameCellValueFactory implements Callback<TableColumn.CellDataFeatures<Users, HBox>, ObservableValue<HBox>> {

        @Override
        public ObservableValue<HBox> call(TableColumn.CellDataFeatures<Users, HBox> param) {
            Users item = param.getValue();

            Region icon = new Region();
            icon.getStyleClass().add("svg-verified-icon");

            String trimmedUsername = EstfxUtil.trimText(item.getUsername(), 10);
            Text name = new Text(trimmedUsername);
            name.getStyleClass().add("text-name");

            HBox container = new HBox(name, icon);
            container.setAlignment(Pos.CENTER);
            container.setSpacing(5);

            if (!item.getUserType().equals("Administrator")) {
                container.getChildren().remove(icon);
            }
            return new SimpleObjectProperty<>(container);
        }
    }

    private class ActionCellValueFactory implements Callback<TableColumn.CellDataFeatures<Users, Button>, ObservableValue<Button>> {

        @Override
        public ObservableValue<Button> call(TableColumn.CellDataFeatures<Users, Button> param) {
            Users item = param.getValue();

            Button action = new Button("View profile");
            action.getStyleClass().add("btn-action");
            action.setOnAction(t -> {
                loadProfile(item.getId());
            });
            return new SimpleObjectProperty<>(action);
        }
    }
}
