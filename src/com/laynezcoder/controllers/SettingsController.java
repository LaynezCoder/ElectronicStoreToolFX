package com.laynezcoder.controllers;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.database.DatabaseConnection;
import com.laynezcoder.database.DatabaseHelper;
import com.laynezcoder.models.Users;
import com.laynezcoder.resources.Resources;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class SettingsController implements Initializable {

    @FXML
    private StackPane stckSettings;

    @FXML
    private AnchorPane rootSettings;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtConfirmPassword;

    @FXML
    private JFXComboBox<String> cmbDialogTransition;

    @FXML
    private JFXTextArea txtBio;

    @FXML
    private JFXButton btnSave;

    @FXML
    private Text title;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setOptionsToComboBox();
        maximumCharacters();
        animationNodes();
        validations();
        selectText();
        setFonts();
        loadData();
    }

    private void loadData() {
        try {
            String sql = "SELECT Users.nameUser, Users.email, Users.pass, Users.biography, Users.dialogTransition "
                    + "FROM Users INNER JOIN UserSession ON UserSession.userId = Users.id WHERE UserSession.id = 1";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                txtName.setText(resultSet.getString("nameUser"));
                txtUser.setText(resultSet.getString("email"));
                txtPassword.setText(resultSet.getString("pass"));
                txtConfirmPassword.setText(resultSet.getString("pass"));
                txtBio.setText(resultSet.getString("biography"));
                startJFXComboBox(resultSet);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
            Resources.showErrorAlert(stckSettings, rootSettings, title, "An error occurred when connecting to MySQL.\n"
                    + "Check your connection to MySQL");
        }
    }

    @FXML
    private void updateCredentials() {
        if (txtName.getText().isEmpty() && txtUser.getText().isEmpty() && txtPassword.getText().isEmpty() && txtConfirmPassword.getText().isEmpty() && txtBio.getText().isEmpty()) {
            shakeAnimation(txtName);
            shakeAnimation(txtUser);
            shakeAnimation(txtPassword);
            shakeAnimation(txtConfirmPassword);
            shakeAnimation(txtBio);
        } else if (txtName.getText().isEmpty()) {
            shakeAnimation(txtName);
        } else if (txtUser.getText().isEmpty()) {
            shakeAnimation(txtUser);
        } else if (txtUser.getText().length() < 4) {
            Resources.notification("Error", "Please enter at least 4 characters", "error.png");
            shakeAnimation(txtUser);
        } else if (txtPassword.getText().isEmpty()) {
            shakeAnimation(txtPassword);
        } else if (txtPassword.getText().length() < 4) {
            Resources.notification("Error", "Please enter at least 4 characters", "error.png");
            shakeAnimation(txtPassword);
        } else if (txtConfirmPassword.getText().isEmpty()) {
            shakeAnimation(txtConfirmPassword);
        } else if (!txtConfirmPassword.getText().equals(txtPassword.getText())) {
            Resources.notification("Error", "Passwords do not match", "error.png");
            shakeAnimation(txtConfirmPassword);
            shakeAnimation(txtPassword);
        } else if (cmbDialogTransition.getSelectionModel().isEmpty()) {
            shakeAnimation(cmbDialogTransition);
        } else if (txtBio.getText().isEmpty()) {
            shakeAnimation(txtBio);
        } else if (DatabaseHelper.checkIfUserExists(txtUser.getText()) != 0 && !txtUser.getText().equals(DatabaseHelper.getUserSession())) {
            Resources.notification("Error", "This user already exists", "error.png");
            shakeAnimation(txtUser);
        } else {
            Users users = new Users();
            users.setId(DatabaseHelper.getIdUserSession());
            users.setNameUser(txtName.getText());
            users.setEmail(txtUser.getText());
            users.setPass(txtPassword.getText());
            users.setBiography(txtBio.getText());
            saveTypeAnimation(users);
            
            boolean result = DatabaseHelper.updateUserFromSettings(users);
            if (result) {
                loadData();
                rootSettings.setDisable(false);
                Resources.showSuccessAlert(stckSettings, rootSettings, rootSettings, "¡Credentials saved successfully!");
            } else {
                Resources.notification("FATAL ERROR", "An error occurred when connecting to MySQL.", "error.png");
            }

        }
    }

    private Users saveTypeAnimation(Users user) {
        String animation = cmbDialogTransition.getSelectionModel().getSelectedItem();

        switch (animation) {
            case "Left":
                user.setDialogTransition("LEFT");
                break;
            case "Right":
                user.setDialogTransition("RIGHT");
                break;
            case "Top":
                user.setDialogTransition("TOP");
                break;
            case "Bottom":
                user.setDialogTransition("BOTTOM");
                break;
            case "Center":
                user.setDialogTransition("CENTER");
                break;
        }

        return user;
    }

    private void startJFXComboBox(ResultSet rs) {
        try {
            String value = rs.getString("dialogTransition");

            switch (value) {
                case "LEFT":
                    cmbDialogTransition.setValue("Left");
                    break;
                case "RIGHT":
                    cmbDialogTransition.setValue("Right");
                    break;
                case "TOP":
                    cmbDialogTransition.setValue("Top");
                    break;
                case "BOTTOM":
                    cmbDialogTransition.setValue("Bottom");
                    break;
                case "CENTER":
                    cmbDialogTransition.setValue("Center");
                    break;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setOptionsToComboBox() {
        cmbDialogTransition.getItems().addAll("Left", "Right", "Top", "Bottom", "Center");
    }

    private void setFonts() {
        Resources.setFontToText(title, 30);
    }

    private void animationNodes() {
        Resources.fadeInUpAnimation(txtName);
        Resources.fadeInUpAnimation(txtUser);
        Resources.fadeInUpAnimation(txtPassword);
        Resources.fadeInUpAnimation(btnSave);
        Resources.fadeInUpAnimation(txtConfirmPassword);
        Resources.fadeInUpAnimation(txtBio);
        Resources.fadeInUpAnimation(cmbDialogTransition);
        Resources.fadeInUpAnimation(title);
    }

    private void shakeAnimation(Node node) {
        new Shake(node).play();
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

    /*
    //action of setting profile image in process
    @FXML
    private void openFileExplorer() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
        
        File selectedFile = fileChooser.showOpenDialog(getStage());
        if (selectedFile != null) {
            try {
                BufferedImage bufferedImage = ImageIO.read(selectedFile);
                BufferedImage image
                        = Scalr.resize(bufferedImage, Scalr.Method.AUTOMATIC, 200, 200);
                
                Image myImage = SwingFXUtils.toFXImage(image, null);
                
                System.out.println("Buffered Image: " + bufferedImage.getWidth() + " " + bufferedImage.getHeight());
                System.out.println("Image Final: " + myImage.getWidth() + " " + myImage.getHeight());
            } catch (IOException ex) {
                Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("file is not valid!");
        }
    }

    private Stage getStage() {
        return (Stage) btnSave.getScene().getWindow();
    }
    */
}
