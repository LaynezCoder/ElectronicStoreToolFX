package com.laynezcoder.estfx.controllers;

import animatefx.animation.FadeOut;
import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.models.Users;
import com.laynezcoder.estfx.resources.Constants;
import com.laynezcoder.resources.Resources;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startConfig();
        StartStepOne();
        setFonts();
        setOptionsToComboBox();
        selectText();
        validations();
        maximumCharacters();
    }

    private void setOptionsToComboBox() {
        cmbDialogTransition.getItems().addAll("Left", "Right", "Top", "Bottom", "Center");
        cmbDialogTransition.setValue("Center");
    }

    private void setFonts() {
        Resources.setFontToText(title, 20);
        Resources.setFontToText(textStep1, 14);
        Resources.setFontToText(textStep2, 14);
        Resources.setFontToText(textProgressBar, 12);
        Resources.setFontToText(textStep3, 12);
        Resources.setFontToText(finish, 12);
        Resources.setFontToText(finishText, 15);
        
        Resources.setFontToJFXButton(btnStep1, 12);
        Resources.setFontToJFXButton(btnBackStep2, 12);
        Resources.setFontToJFXButton(btnStep3, 12);
        Resources.setFontToJFXButton(btnDialogStep3, 12);
        Resources.setFontToJFXButton(btnBackStep2, 12);
        Resources.setFontToJFXButton(btnBackStep3, 12);
        Resources.setFontToJFXButton(btnStart, 12);
    }

    private void startConfig() {
        textProgressBar.setText("1 of 3");
        progressBar.setProgress(0.00);

        Resources.fadeInUpAnimation(title);
        Resources.fadeInUpAnimation(textProgressBar);
        Resources.fadeInUpAnimation(progressBar);
    }

    @FXML
    private void StartStepOne() {
        paneStep1.setVisible(true);
        paneStep2.setVisible(false);

        textProgressBar.setText("1 of 3");
        progressBar.setProgress(0.00);

        Resources.fadeInUpAnimation(paneStep1);
        Resources.fadeInUpAnimation(paneControlsStep1);
        Resources.fadeInUpAnimation(textStep1);
        Resources.fadeInUpAnimation(btnStep1);
    }

    @FXML
    private void stepOneToStepTwo() {
        if (txtName.getText().isEmpty()) {
            Resources.notification("Error", "Insufficient data", "error.png");
            new Shake(txtName).play();
        } else if (txtUser.getText().isEmpty()) {
            Resources.notification("Error", "Insufficient data", "error.png");
            new Shake(txtUser).play();
        } else if (txtUser.getText().length() < 4) {
            Resources.notification("Error", "Please enter at least 4 characters", "error.png");
            new Shake(txtUser).play();
        } else if (txtPassword.getText().isEmpty()) {
            Resources.notification("Error", "Insufficient data", "error.png");
            new Shake(txtPassword).play();
        } else if (txtPassword.getText().length() < 4) {
            Resources.notification("Error", "Please enter at least 4 characters", "error.png");
            new Shake(txtPassword).play();
        } else if (txtConfirmPassword.getText().isEmpty()) {
            Resources.notification("Error", "Insufficient data", "error.png");
            new Shake(txtConfirmPassword).play();
        } else if (!txtConfirmPassword.getText().equals(txtPassword.getText())) {
            Resources.notification("Error", "Passwords do not match", "error.png");
            new Shake(txtConfirmPassword).play();
            new Shake(txtPassword).play();
        } else {
            startStepTwo();
        }
    }

    @FXML
    private void startStepTwo() {
        paneStep1.setVisible(false);
        paneStep2.setVisible(true);
        paneStep3.setVisible(false);

        textProgressBar.setText("2 of 3");
        progressBar.setProgress(0.33);

        Resources.fadeInUpAnimation(paneStep2);
        Resources.fadeInUpAnimation(textStep2);
        Resources.fadeInUpAnimation(txtBio);
        Resources.fadeInUpAnimation(hBoxStep2);
    }

    @FXML
    private void stepTwoToStepThree() {
        if (txtBio.getText().isEmpty()) {
            Resources.notification("Error", "Insufficient data", "error.png");
            new Shake(txtBio).play();
        } else {
            startStepThree();
        }
    }

    private void startStepThree() {
        paneStep2.setVisible(false);
        paneStep3.setVisible(true);

        textProgressBar.setText("3 of 3");
        progressBar.setProgress(0.66);

        Resources.fadeInUpAnimation(paneStep3);
        Resources.fadeInUpAnimation(textStep3);
        Resources.fadeInUpAnimation(cmbDialogTransition);
        Resources.fadeInUpAnimation(hBoxStep3);
    }

    @FXML
    private void finish() {
        insertUserInDB();
        DatabaseHelper.insertUsserSession(1);

        paneStep3.setVisible(false);
        paneFinish.setVisible(true);
        textProgressBar.setText("Finalized");
        progressBar.setProgress(1);

        textProgressBar.setLayoutX(599);
        textProgressBar.setLayoutY(553);

        Resources.fadeInUpAnimation(paneFinish);
        Resources.fadeInUpAnimation(spinner);

        FadeOut fadeOut1 = new FadeOut(btnStart);
        fadeOut1.setSpeed(10);
        fadeOut1.play();

        FadeOut fadeOut2 = new FadeOut(finishText);
        fadeOut2.setSpeed(10);
        fadeOut2.play();

        PauseTransition pt = new PauseTransition(Duration.seconds(5));
        pt.setOnFinished(ev -> {
            new FadeOut(spinner).play();
            Resources.fadeInUpAnimation(btnStart);
            Resources.fadeInUpAnimation(finishText);
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
        users.setNameUser(txtName.getText());
        users.setEmail(txtUser.getText());
        users.setPass(txtPassword.getText());
        users.setBiography(txtBio.getText());
        users.setDialogTransition(getDialogTransition());
        users.setUserType("Administrator");
        users.setProfileImage(StartController.class.getResourceAsStream(Constants.PROFILE_PICTURES_PACKAGE + "a.png"));
        try {
            String sql = "INSERT INTO Users (nameUser, email, pass, userType, profileImage) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = DatabaseConnection.getInstance().getConnection().prepareCall(sql);
            stmt.setString(1, users.getNameUser());
            stmt.setString(2, users.getEmail());
            stmt.setString(3, users.getPass());
            stmt.setString(4, users.getUserType());
            stmt.setBlob(5, users.getProfileImage());
            stmt.execute();
            updateUserInDB(users);
        } catch (SQLException ex) {
            Logger.getLogger(StartController.class.getName()).log(Level.SEVERE, null, ex);
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
            Parent root = FXMLLoader.load(getClass().getResource("/com/laynezcoder/views/MainView.fxml"));
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.DECORATED);
            stage.setScene(scene);
            stage.setTitle("Electronic Store Tool FX");
            stage.getIcons().add(new Image(Resources.SOURCE_PACKAGES + "/media/reicon.png"));
            stage.show();
            closeStage();
            
            root.setOnKeyPressed((KeyEvent e) -> {
                if (e.getCode() == KeyCode.F11) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            });
            
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
        button.getStylesheets().add(Resources.LIGHT_THEME);
        button.getStyleClass().add("button-start-dialog");

        String body = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        JFXDialog dialog = new JFXDialog(stckStart, dialogLayout, JFXDialog.DialogTransition.valueOf(getDialogTransition()));
        dialogLayout.setBody(new Label(body));
        dialogLayout.setActions(button);
        Resources.setStyleToAlerts(dialog);
        dialog.show();

        button.setOnMouseClicked(ev -> {
            dialog.close();
        });
    }

    private void selectText() {
        Resources.selectTextToJFXTextField(txtName);
        Resources.selectTextToJFXTextField(txtUser);
        Resources.selectTextToJFXPasswordField(txtPassword);
        Resources.selectTextToJFXPasswordField(txtConfirmPassword);
        Resources.selectTextToJFXTextArea(txtBio);
    }

    private void validations() {
        Resources.validationOfJFXTextArea(txtBio);
        Resources.validationOfJFXComboBox(cmbDialogTransition);
        Resources.validationOfJFXTextField(txtName);
        Resources.validationOfJFXTextField(txtUser);
        Resources.validationOfJFXPasswordField(txtPassword);
        Resources.validationOfJFXPasswordField(txtConfirmPassword);
    }

    private void maximumCharacters() {
        Resources.limitTextField(txtName, 20);
        Resources.limitTextField(txtUser, 20);
        Resources.limitJFXPasswordField(txtPassword, 20);
        Resources.limitJFXPasswordField(txtConfirmPassword, 20);
    }
}
