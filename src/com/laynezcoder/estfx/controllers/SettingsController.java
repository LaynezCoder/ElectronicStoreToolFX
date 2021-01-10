package com.laynezcoder.estfx.controllers;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.laynezcoder.estfx.alerts.AlertType;
import com.laynezcoder.estfx.alerts.AlertsBuilder;
import com.laynezcoder.estfx.animations.Animations;
import com.laynezcoder.estfx.database.DatabaseConnection;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.fonts.Fonts;
import com.laynezcoder.estfx.mask.RequieredFieldsValidators;
import com.laynezcoder.estfx.mask.TextFieldMask;
import com.laynezcoder.estfx.models.Users;
import com.laynezcoder.estfx.notifications.NotificationType;
import com.laynezcoder.estfx.notifications.NotificationsBuilder;
import com.laynezcoder.estfx.preferences.Preferences;
import com.laynezcoder.estfx.resources.Constants;
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

    private final String MESSAGE_PROFILE_IMAGE_SAVED = "Success, profile picture saved.";

    private final String MESSAGE_PASSWORDS_NOT_MATCH = "Passwords do not match.";

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
        setMask();
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
            ps.setInt(1, DatabaseHelper.getSessionId());
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
            AlertsBuilder.create(AlertType.ERROR, stckSettings, rootSettings, txtBio, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
        }
    }

    @FXML
    private void updateCredentials() {
        String name = txtName.getText().trim();
        String user = txtUser.getText().trim();
        String password = txtPassword.getText().trim();
        String confirmPassword = txtConfirmPassword.getText().trim();
        String bio = txtBio.getText().trim();

        if (name.isEmpty() && user.isEmpty() && password.isEmpty() && confirmPassword.isEmpty() && bio.isEmpty()) {
            Animations.shake(txtName);
            Animations.shake(txtUser);
            Animations.shake(txtPassword);
            Animations.shake(txtConfirmPassword);
            Animations.shake(txtBio);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.INSUFFICIENT_DATA);
            return;
        }

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
            txtUser.requestFocus();
            Animations.shake(txtUser);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ENTER_AT_LEAST_4_CHARACTERES);
            return;
        }

        if (password.isEmpty()) {
            txtPassword.requestFocus();
            Animations.shake(txtPassword);
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
            return;
        }

        if (!confirmPassword.equals(password)) {
            txtConfirmPassword.requestFocus();
            Animations.shake(txtPassword);
            Animations.shake(txtConfirmPassword);
            NotificationsBuilder.create(NotificationType.ERROR, MESSAGE_PASSWORDS_NOT_MATCH);
            return;
        }

        if (cmbDialogTransition.getSelectionModel().isEmpty()) {
            Animations.shake(cmbDialogTransition);
            return;
        }

        if (bio.isEmpty()) {
            txtBio.requestFocus();
            Animations.shake(txtBio);
            return;
        }

        if (DatabaseHelper.checkIfUserExists(user) != 0 && !user.equals(DatabaseHelper.getSessionUsername())) {
            txtUser.requestFocus();
            Animations.shake(txtUser);
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_USER_ALREADY_EXISTS);
            return;
        }

        Users users = new Users();
        users.setId(DatabaseHelper.getSessionId());
        users.setNameUser(name);
        users.setEmail(user);
        users.setPass(password);
        users.setBiography(bio);
        users.setDialogTransition(getDialogTransition());

        boolean result = DatabaseHelper.updateUserFromSettings(users);
        if (result) {
            loadData();
            AlertsBuilder.create(AlertType.SUCCES, stckSettings, rootSettings, rootSettings, Constants.MESSAGE_ADDED);
        } else {
            NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
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
        Fonts.toButton(btnSave, 14);
        Fonts.toText(textName, 16);
        Fonts.toText(textUserType, 12);
    }

    private void animationNodes() {
        Animations.fadeInUp(txtName);
        Animations.fadeInUp(txtUser);
        Animations.fadeInUp(txtPassword);
        Animations.fadeInUp(btnSave);
        Animations.fadeInUp(txtConfirmPassword);
        Animations.fadeInUp(txtBio);
        Animations.fadeInUp(cmbDialogTransition);
        Animations.fadeInUp(headerContainer);
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
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtPassword, 40);
        TextFieldMask.onlyNumbersAndLettersNotSpaces(txtConfirmPassword, 40);
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
                        AlertsBuilder.create(AlertType.SUCCES, stckSettings, rootSettings, rootSettings, MESSAGE_PROFILE_IMAGE_SAVED);
                        loadProfileImage();
                    } else {
                        NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
                    }
                    setInitialDirectory();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                NotificationsBuilder.create(NotificationType.ERROR, Constants.MESSAGE_ERROR_CONNECTION_MYSQL);
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
