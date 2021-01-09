package com.laynezcoder.controllers;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.models.Users;
import com.laynezcoder.estfx.preferences.Preferences;
import com.laynezcoder.resources.Resources;
import com.laynezcoder.estfx.util.CropImageProfile;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SettingsController implements Initializable {
    
    private final ColorAdjust colorAdjust = new ColorAdjust();
    
    private final long LIMIT = 1000000;

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
    private Group imageProfileContainer;

    @FXML
    private HBox headerContainer;

    @FXML
    private Text textName;

    @FXML
    private Text textUserType;

    @FXML
    private ImageView imageViewProfile;
    
    @FXML
    private MaterialDesignIconView icon;

    private File imageFile;    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animationNodes();
        selectText();
        setFonts();
        loadData();
        validations();
        maximumCharacters();
        setOptionsToComboBox();
        initializeProfileImage();
        effectEditImageProfile();
    }

    private void effectEditImageProfile() {
        imageProfileContainer.hoverProperty().addListener((o, oldV, newV) -> {
            if (!oldV) {
                colorAdjust.setBrightness(-0.5);
                imageViewProfile.setEffect(colorAdjust);
                icon.setVisible(true);
            } else {
                imageViewProfile.setEffect(null);
                icon.setVisible(false);
            }
        });
    }

    private void initializeProfileImage() {
        loadProfileImage();
        Circle circle = new Circle(45);
        circle.setCenterX(imageViewProfile.getFitWidth() / 2);
        circle.setCenterY(imageViewProfile.getFitHeight() / 2);
        imageViewProfile.setClip(circle);
    }

    private void loadProfileImage() {
        Image image = null;
        try {
            String sql = "SELECT profileImage FROM Users WHERE id = ?";
            PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ps.setInt(1, DatabaseHelper.getIdUserSession());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                InputStream img = rs.getBinaryStream("profileImage");
                if (img != null) {
                    image = new Image(img, 100, 100, true, true);
                }
            }
            imageViewProfile.setImage(image);
        } catch (SQLException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadData() {
        try {
            String sql = "SELECT Users.nameUser, Users.email, Users.pass, Users.biography, Users.dialogTransition, Users.userType "
                    + "FROM Users INNER JOIN UserSession ON UserSession.userId = Users.id WHERE UserSession.id = 1";
            PreparedStatement preparedStatement = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                textName.setText(rs.getString("nameUser"));
                textUserType.setText(rs.getString("userType"));
                txtName.setText(rs.getString("nameUser"));
                txtUser.setText(rs.getString("email"));
                txtPassword.setText(rs.getString("pass"));
                txtConfirmPassword.setText(rs.getString("pass"));
                txtBio.setText(rs.getString("biography"));
                initializeJFXComboBox(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
            Resources.showErrorAlert(stckSettings, rootSettings, txtBio, "An error occurred when connecting to MySQL.\n"
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
            users.setDialogTransition(getDialogTransition());

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

    private String getDialogTransition() {
        return cmbDialogTransition.getSelectionModel().getSelectedItem().toUpperCase();
    }

    private void initializeJFXComboBox(ResultSet rs) {
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
        Resources.setFontToText(textName, 16);
        Resources.setFontToText(textUserType, 12);
        Resources.setFontToJFXButton(btnSave, 14);
    }

    private void animationNodes() {
        Resources.fadeInUpAnimation(txtName);
        Resources.fadeInUpAnimation(txtUser);
        Resources.fadeInUpAnimation(txtPassword);
        Resources.fadeInUpAnimation(btnSave);
        Resources.fadeInUpAnimation(txtConfirmPassword);
        Resources.fadeInUpAnimation(txtBio);
        Resources.fadeInUpAnimation(cmbDialogTransition);
        Resources.fadeInUpAnimation(headerContainer);
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

    @FXML
    private void showFileChooser() {
        imageFile = getImageFromFileChooser(getStage());
        if (imageFile != null) {
            if (imageFile.length() < LIMIT) {
                try {
                    CropImageProfile crop = new CropImageProfile(imageFile);
                    boolean result = DatabaseHelper.updateImageFromSettings(crop.getInputStream());
                    if (result) {
                        Resources.showSuccessAlert(stckSettings, rootSettings, rootSettings, "¡Credentials saved successfully!");
                        loadProfileImage();
                    } else {
                        Resources.notification("FATAL ERROR", "An error occurred when connecting to MySQL.", "error.png");
                    }
                    setInitialDirectory();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Resources.notification("FATAL ERROR", "Please upload a picture smaller than 1 MB.", "error.png");
            }
        }
    }

    private Stage getStage() {
        return (Stage) btnSave.getScene().getWindow();
    }

    private void setInitialDirectory() {
        Preferences preferences = Preferences.getPreferences();
        preferences.setInitialPathFileChooserSettingsController(imageFile.getParent());
        Preferences.writePreferencesToFile(preferences);
    }

    private File getImageFromFileChooser(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterImages = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().addAll(extFilterImages);
        fileChooser.setInitialDirectory(getInitialDirectoy());
        fileChooser.setTitle("Select an image");

        File selectedImage = fileChooser.showOpenDialog(stage);
        return selectedImage;
    }

    private File getInitialDirectoy() {
        Preferences preferences = Preferences.getPreferences();
        File initPath = new File(preferences.getInitialPathFileChooserSettingsController());
        if (!initPath.exists()) {
            preferences.setInitialPathFileChooserSettingsController(System.getProperty("user.home"));
            Preferences.writePreferencesToFile(preferences);
            initPath = new File(preferences.getInitialPathFileChooserSettingsController());
        }
        return initPath;
    }
}
