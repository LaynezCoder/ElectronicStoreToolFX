package com.laynezcoder.estfx.controllers;

import animatefx.animation.FadeOut;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.fonts.Fonts;
import com.laynezcoder.estfx.mask.RequieredFieldsValidators;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.Users;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.resources.Constants;
import com.laynezcoder.estfx.util.DefaultProfileImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class StartController implements Initializable {

    @FXML
    private StackPane stckStart;

    @FXML
    private Text title;

    @FXML
    private Pane paneStep1;

    @FXML
    private Text textStep1;

    @FXML
    private Pane paneControlsStep1;

    @FXML
    private JFXButton btnStep1;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXPasswordField txtConfirmPassword;

    @FXML
    private Pane paneStep2;

    @FXML
    private Text textStep2;

    @FXML
    private JFXTextArea txtBio;

    @FXML
    private HBox hBoxStep2;

    @FXML
    private JFXButton btnBackStep2;

    @FXML
    private JFXButton btnStep2;

    @FXML
    private Pane paneStep3;

    @FXML
    private Text textStep3;

    @FXML
    private JFXComboBox<String> cmbDialogTransition;

    @FXML
    private HBox hBoxStep3;

    @FXML
    private JFXButton btnDialogStep3;

    @FXML
    private JFXButton btnStep3;

    @FXML
    private JFXButton btnBackStep3;

    @FXML
    private Pane paneFinish;

    @FXML
    private Text finish;

    @FXML
    private Text finishText;

    @FXML
    private JFXSpinner spinner;

    @FXML
    private JFXButton btnStart;

    @FXML
    private JFXProgressBar progressBar;

    @FXML
    private Text textProgressBar;

    private double x, y;

    private String name, user, password, confirmPassword, bio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startConfig();
        setFonts();
        selectText();
        setMask();
        validations();
        StartStepOne();
        setOptionsToComboBox();
    }

    private void setOptionsToComboBox() {
        cmbDialogTransition.getItems().addAll("Left", "Right", "Top", "Bottom", "Center");
        cmbDialogTransition.setValue("Center");
    }

    private void setFonts() {
        Fonts.toText(title, 20);
        Fonts.toText(textStep1, 14);
        Fonts.toText(textStep2, 14);
        Fonts.toText(textProgressBar, 12);
        Fonts.toText(textStep3, 12);
        Fonts.toText(finish, 12);
        Fonts.toText(finishText, 15);
        Fonts.toButton(btnStep1, 12);
        Fonts.toButton(btnBackStep2, 12);
        Fonts.toButton(btnStep3, 12);
        Fonts.toButton(btnDialogStep3, 12);
        Fonts.toButton(btnBackStep2, 12);
        Fonts.toButton(btnBackStep3, 12);
        Fonts.toButton(btnStart, 12);
    }

    private void startConfig() {
        textProgressBar.setText("1 of 3");
        progressBar.setProgress(0.00);

        Animations.fadeInUp(title);
        Animations.fadeInUp(textProgressBar);
        Animations.fadeInUp(progressBar);
    }

    @FXML
    private void StartStepOne() {
        paneStep1.setVisible(true);
        paneStep2.setVisible(false);

        textProgressBar.setText("1 of 3");
        progressBar.setProgress(0.00);

        Animations.fadeInUp(paneStep1);
        Animations.fadeInUp(paneControlsStep1);
        Animations.fadeInUp(textStep1);
        Animations.fadeInUp(btnStep1);
    }

    @FXML
    private void stepOneToStepTwo() {
        name = txtName.getText().trim();
        user = txtUser.getText().trim();
        password = txtPassword.getText().trim();
        confirmPassword = txtConfirmPassword.getText().trim();

        if (name.isEmpty()) {
            txtName.requestFocus();
            Animations.shake(txtName);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        if (user.isEmpty()) {
            txtUser.requestFocus();
            Animations.shake(txtUser);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        if (user.length() < 4) {
            txtUser.requestFocus();
            Animations.shake(txtUser);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ENTER_AT_LEAST_4_CHARACTERES);
            return;
        }

        if (password.isEmpty()) {
            txtPassword.requestFocus();
            Animations.shake(txtPassword);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        if (password.length() < 4) {
            txtPassword.requestFocus();
            Animations.shake(txtPassword);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ENTER_AT_LEAST_4_CHARACTERES);
            return;
        }

        if (confirmPassword.isEmpty()) {
            txtConfirmPassword.requestFocus();
            Animations.shake(txtConfirmPassword);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        if (!confirmPassword.equals(password)) {
            Animations.shake(txtConfirmPassword);
            Animations.shake(txtPassword);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_PASSWORDS_NOT_MATCH);
            return;
        }

        startStepTwo();
    }

    @FXML
    private void startStepTwo() {
        paneStep1.setVisible(false);
        paneStep2.setVisible(true);
        paneStep3.setVisible(false);

        textProgressBar.setText("2 of 3");
        progressBar.setProgress(0.33);

        Animations.fadeInUp(paneStep2);
        Animations.fadeInUp(textStep2);
        Animations.fadeInUp(txtBio);
        Animations.fadeInUp(hBoxStep2);
    }

    @FXML
    private void stepTwoToStepThree() {
        bio = txtBio.getText().trim();
        if (bio.isEmpty()) {
            Animations.shake(txtBio);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_INSUFFICIENT_DATA);
            return;
        }

        startStepThree();
    }

    private void startStepThree() {
        paneStep2.setVisible(false);
        paneStep3.setVisible(true);

        textProgressBar.setText("3 of 3");
        progressBar.setProgress(0.66);

        Animations.fadeInUp(paneStep3);
        Animations.fadeInUp(textStep3);
        Animations.fadeInUp(cmbDialogTransition);
        Animations.fadeInUp(hBoxStep3);
    }

    @FXML
    private void finish() {
        insertUserInDB();
        DatabaseHelper.insertUserSession(1);

        paneStep3.setVisible(false);
        paneFinish.setVisible(true);
        textProgressBar.setText("Finalized");
        progressBar.setProgress(1);

        textProgressBar.setLayoutX(599);
        textProgressBar.setLayoutY(553);

        Animations.fadeInUp(paneFinish);
        Animations.fadeInUp(spinner);

        FadeOut fadeOut1 = new FadeOut(btnStart);
        fadeOut1.setSpeed(10);
        fadeOut1.play();

        FadeOut fadeOut2 = new FadeOut(finishText);
        fadeOut2.setSpeed(10);
        fadeOut2.play();

        PauseTransition pt = new PauseTransition(Duration.seconds(5));
        pt.setOnFinished(ev -> {
            new FadeOut(spinner).play();
            Animations.fadeInUp(btnStart);
            Animations.fadeInUp(finishText);
        });
        pt.play();
    }

    @FXML
    private void dragged(MouseEvent event) {
        Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stg.setX(event.getScreenX() - x);
        stg.setY(event.getScreenY() - y);
    }

    private void insertUserInDB() {
        Users users = new Users();
        users.setNameUser(name);
        users.setEmail(user);
        users.setPass(password);
        users.setBiography(bio);
        users.setDialogTransition(getDialogTransition());
        users.setUserType("Administrator");

        try {
            users.setProfileImage(DefaultProfileImage.getImage(name));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
        }

        boolean result = DatabaseHelper.insertNewUser(users);
        if (result) {
            updateUserInDB(users);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }
    }

    private void updateUserInDB(Users users) {
        try {
            String sql = "UPDATE Users SET biography = ?, dialogTransition = ? WHERE id = 1";
            PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareCall(sql);
            stmt.setString(1, users.getBiography());
            stmt.setString(2, users.getDialogTransition());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getDialogTransition() {
        return cmbDialogTransition.getSelectionModel().getSelectedItem().toUpperCase();
    }

    @FXML
    private void mainWindow() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(Constants.MAIN_VIEW));
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.DECORATED);
            stage.setScene(scene);
            stage.setMinHeight(Constants.MIN_HEIGHT);
            stage.setMinWidth(Constants.MIN_WIDTH);
            stage.setTitle(Constants.TITLE);
            stage.getIcons().add(new Image(Constants.STAGE_ICON));
            stage.show();
            closeStage();

            root.setOnKeyPressed((KeyEvent e) -> {
                if (e.getCode() == KeyCode.F11) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            });
            
            NotificationsBuilder.create(NotificationType.SUCCESS, "Welcome to the system " + name + "!");
        } catch (IOException ex) {
            Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeStage() {
        ((Stage) txtUser.getScene().getWindow()).close();
    }

    @FXML
    private void closeWindow() {
        System.exit(0);
    }

    @FXML
    private void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    private void alert() {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();

        JFXButton button = new JFXButton("¡ok!");
        button.getStylesheets().add(Constants.LIGHT_THEME);
        button.getStyleClass().add("button-start-dialog");

        String body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        JFXDialog dialog = new JFXDialog(stckStart, dialogLayout, JFXDialog.DialogTransition.valueOf(getDialogTransition()));
        dialogLayout.setBody(new Label(body));
        dialogLayout.setActions(button);
        dialog.getStylesheets().add(Constants.LIGHT_THEME);
        dialog.getStyleClass().add("jfx-dialog-overlay-pane");
        dialog.show();

        button.setOnMouseClicked(ev -> {
            dialog.close();
        });
    }

    private void selectText() {
        TextFieldMask.selectText(txtName);
        TextFieldMask.selectText(txtUser);
        TextFieldMask.selectText(txtPassword);
        TextFieldMask.selectText(txtConfirmPassword);
        TextFieldMask.selectTextToJFXTextArea(txtBio);
    }

    private void validations() {
        RequieredFieldsValidators.toJFXTextArea(txtBio);
        RequieredFieldsValidators.toJFXComboBox(cmbDialogTransition);
        RequieredFieldsValidators.toJFXTextField(txtName);
        RequieredFieldsValidators.toJFXTextField(txtUser);
        RequieredFieldsValidators.toJFXPasswordField(txtPassword);
        RequieredFieldsValidators.toJFXPasswordField(txtConfirmPassword);
    }

    private void setMask() {
        TextFieldMask.onlyLetters(txtName, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtUser, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtConfirmPassword, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtPassword, 40);
    }
}
