package com.laynezcoder.resources;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInUp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.laynezcoder.database.DatabaseHelper;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Resources {

    public static final String SOURCE_PACKAGES = "/com/laynezcoder";
    public static final String PACKAGE_MEDIA = "/com/laynezcoder/media/";
    public static final String RIMOUSKI_FONT = "/com/laynezcoder/fonts/rimouski.ttf";
    public static final String NO_IMAGE_AVAILABLE = "/com/laynezcoder/media/empty-image.jpg";
    public static final String CSS_LIGHT_THEME = "/com/laynezcoder/resources/LightTheme.css";
    public static final String LIGHT_THEME = Resources.class.getResource(CSS_LIGHT_THEME).toExternalForm();
    public static JFXDialog dialog;

    public static void showSuccessAlert(StackPane dialogContainer, Node nodeToBlur, Node nodeToDisable, String text) {
        Font font = Font.loadFont(Resources.class.getResourceAsStream(RIMOUSKI_FONT), 17);

        AnchorPane root = new AnchorPane();
        root.setPrefSize(390, 230);
        root.getStylesheets().add(LIGHT_THEME);

        JFXButton button = new JFXButton("Okey");
        button.getStyleClass().add("button-alert-success");
        button.setFont(font);

        HBox hBox = new HBox();
        hBox.setLayoutY(115);
        hBox.setPrefSize(390, 115);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(button);

        Text textTitle = new Text("Succes!");
        textTitle.getStyleClass().add("title-alert-success");
        textTitle.setFont(font);

        Text textBody = new Text(text);
        textBody.getStyleClass().add("body-alert-success");
        textBody.setFont(font);

        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPrefSize(390, 115);
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.setPadding(new Insets(0, 0, 0, 30));
        vBox.getChildren().addAll(textTitle, textBody);

        root.getChildren().addAll(hBox, vBox);

        BoxBlur blur = new BoxBlur(3, 3, 3);

        dialog = new JFXDialog();
        dialog.setContent(root);
        dialog.setDialogContainer(dialogContainer);
        dialog.setBackground(Background.EMPTY);
        dialog.setTransitionType(DatabaseHelper.dialogTransition());

        setStyleToAlerts(dialog);
        nodeToDisable.setDisable(true);
        nodeToBlur.setEffect(blur);

        dialog.show();

        button.setOnMouseClicked(e -> {
            dialog.close();
        });

        dialog.setOnDialogOpened(e -> {
            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(blur);
        });

        dialog.setOnDialogClosed(e -> {
            nodeToDisable.setDisable(false);
            nodeToBlur.setEffect(null);
        });
    }

    public static void showErrorAlert(StackPane dialogContainer, Node nodeToBlur, Node nodeToDisable, String text) {
        Font font = Font.loadFont(Resources.class.getResourceAsStream(RIMOUSKI_FONT), 17);

        AnchorPane root = new AnchorPane();
        root.setPrefSize(390, 230);
        root.getStylesheets().add(LIGHT_THEME);

        JFXButton button = new JFXButton("Okey");
        button.getStyleClass().add("button-alert-error");
        button.setFont(font);

        HBox hBox = new HBox();
        hBox.setLayoutY(115);
        hBox.setPrefSize(390, 115);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(button);

        Text textTitle = new Text("Oops!");
        textTitle.getStyleClass().add("title-alert-error");
        textTitle.setFont(font);

        Text textBody = new Text(text);
        textBody.getStyleClass().add("body-alert-error");
        textBody.setFont(font);

        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPrefSize(390, 115);
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.setPadding(new Insets(0, 0, 0, 30));
        vBox.getChildren().addAll(textTitle, textBody);

        root.getChildren().addAll(hBox, vBox);

        BoxBlur blur = new BoxBlur(3, 3, 3);

        dialog = new JFXDialog();
        dialog.setContent(root);
        dialog.setDialogContainer(dialogContainer);
        dialog.setBackground(Background.EMPTY);
        dialog.setTransitionType(DatabaseHelper.dialogTransition());

        setStyleToAlerts(dialog);
        nodeToDisable.setDisable(true);
        nodeToBlur.setEffect(blur);

        dialog.show();

        button.setOnMouseClicked(e -> {
            dialog.close();
        });

        dialog.setOnDialogOpened(e -> {
            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(blur);
        });

        dialog.setOnDialogClosed(e -> {
            nodeToDisable.setDisable(false);
            nodeToBlur.setEffect(null);
        });
    }

    public static void notification(String title, String text, String iconName) {
        Image img = new Image(PACKAGE_MEDIA + iconName);
        Notifications notificationsBuilder = Notifications.create();
        notificationsBuilder.title(title);
        notificationsBuilder.text(text);
        notificationsBuilder.graphic(new ImageView(img));
        notificationsBuilder.hideAfter(Duration.seconds(6));
        notificationsBuilder.position(Pos.BASELINE_RIGHT);
        notificationsBuilder.show();
    }

    public static void tooltip(Node node, AnchorPane tooltip) {
        node.setOnMouseEntered(ev -> {
            FadeIn fadeIn = new FadeIn(tooltip);
            fadeIn.setSpeed(3);
            fadeIn.play();
            tooltip.setVisible(true);
        });

        node.setOnMouseExited(ev -> {
            tooltip.setVisible(false);
        });
    }

    public static void validationOfJFXTextField(JFXTextField txt) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Obligatory field");
        txt.getValidators().add(validator);

        FontAwesomeIconView warnIcon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
        validator.setIcon(warnIcon);

        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                txt.validate();
            }
        });
    }

    public static void validationOfJFXPasswordField(JFXPasswordField txt) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Obligatory field");
        txt.getValidators().add(validator);

        FontAwesomeIconView warnIcon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
        validator.setIcon(warnIcon);

        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                txt.validate();
            }
        });
    }

    public static void validationOfJFXTextArea(JFXTextArea txt) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Obligatory field");
        txt.getValidators().add(validator);

        FontAwesomeIconView warnIcon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
        validator.setIcon(warnIcon);

        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                txt.validate();
            }
        });
    }

    public static void validationOfJFXComboBox(JFXComboBox comboBox) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Obligatory field");
        comboBox.getValidators().add(validator);

        FontAwesomeIconView warnIcon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
        validator.setIcon(warnIcon);

        comboBox.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                comboBox.validate();
            }
        });
    }

    public static void limitTextField(TextField textField, int limit) {
        UnaryOperator<TextFormatter.Change> textLimitFilter = change -> {
            if (change.isContentChange()) {
                int newLength = change.getControlNewText().length();
                if (newLength > limit) {
                    String trimmedText = change.getControlNewText().substring(0, limit);
                    change.setText(trimmedText);
                    int oldLength = change.getControlText().length();
                    change.setRange(0, oldLength);
                }
            }
            return change;
        };
        textField.setTextFormatter(new TextFormatter(textLimitFilter));
    }

    public static void limitJFXPasswordField(JFXPasswordField textField, int limit) {
        UnaryOperator<TextFormatter.Change> textLimitFilter = change -> {
            if (change.isContentChange()) {
                int newLength = change.getControlNewText().length();
                if (newLength > limit) {
                    String trimmedText = change.getControlNewText().substring(0, limit);
                    change.setText(trimmedText);
                    int oldLength = change.getControlText().length();
                    change.setRange(0, oldLength);
                }
            }
            return change;
        };
        textField.setTextFormatter(new TextFormatter(textLimitFilter));
    }

    public static void doubleNumbersValidationTextField(TextField txt) {
        txt.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d{0,5}([\\.]\\d{0,2})?")) {
                txt.setText(oldValue);
            }
        });
    }

    public static void doubleNumbersValidation(TextField txt) {
        txt.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d{0,10}([\\.]\\d{0,2})?")) {
                txt.setText(oldValue);
            }
        });
    }

    public static void validationOnlyNumbers(TextField txt) {
        txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txt.setText(oldValue);
            }
        });
    }

    public static void setTextIsEmpty(TextField text) {
        text.focusedProperty().addListener(ev -> {
            if (text.isFocused() && text.getText().isEmpty()) {
                text.setText("0");
            }
        });
    }

    public static void onlyLettersTextField(TextField txt) {
        txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\sa-zA-Z*") || txt.getText().isEmpty()) {
                txt.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
            }
        });
    }

    public static void noInitSpace(TextField txt) {
        txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (txt.getText().equals(" ")) {
                txt.setText(oldValue);
            }
        });
    }

    public static void selectTextToJFXTextField(JFXTextField txt) {
        txt.focusedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldVal, Boolean newVal) -> {
            Platform.runLater(() -> {
                if (txt.isFocused() && !txt.getText().isEmpty()) {
                    txt.selectAll();
                }
            });
        });
    }

    public static void selectTextToTextField(TextField txt) {
        txt.focusedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldVal, Boolean newVal) -> {
            Platform.runLater(() -> {
                if (txt.isFocused() && !txt.getText().isEmpty()) {
                    txt.selectAll();
                }
            });
        });
    }

    public static void selectTextToJFXTextArea(JFXTextArea txt) {
        txt.focusedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldVal, Boolean newVal) -> {
            Platform.runLater(() -> {
                if (txt.isFocused() && !txt.getText().isEmpty()) {
                    txt.selectAll();
                }
            });
        });
    }

    public static void selectTextToJFXPasswordField(JFXPasswordField txt) {
        txt.focusedProperty().addListener((ObservableValue<? extends Boolean> o, Boolean oldVal, Boolean newVal) -> {
            Platform.runLater(() -> {
                if (txt.isFocused() && !txt.getText().isEmpty()) {
                    txt.selectAll();
                }
            });
        });
    }

    public static void setStyleToAlerts(JFXDialog alert) {
        alert.getStylesheets().add(LIGHT_THEME);
        alert.getStyleClass().add("jfx-dialog-overlay-pane");
    }

    public static void setFontToText(Text text, int sizeFont) {
        Font font = Font.loadFont(Resources.class.getResourceAsStream(RIMOUSKI_FONT), sizeFont);
        text.setFont(font);
    }

    public static void setFontToJFXButton(JFXButton btn, int sizeFont) {
        Font font = Font.loadFont(Resources.class.getResourceAsStream(RIMOUSKI_FONT), sizeFont);
        btn.setFont(font);
    }

    public static void hoverAnimation(Node node, int duration, double setXAndY) {
        ScaleTransition scaleTrans = new ScaleTransition(Duration.millis(duration), node);
        scaleTrans.setFromX(1.0);
        scaleTrans.setFromY(1.0);
        scaleTrans.setToX(setXAndY);
        scaleTrans.setToY(setXAndY);

        node.setOnMouseEntered(ev -> {
            scaleTrans.setRate(1.0);
            scaleTrans.play();
        });

        node.setOnMouseExited(ev -> {
            scaleTrans.setRate(-1.0);
            scaleTrans.play();
        });
    }

    public static void fadeInUpAnimation(Node node) {
        new FadeInUp(node).play();
    }

    public static void translateTransition(double translateByX, double durationInMillis, Node nodeHover, Node icon) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(durationInMillis), icon);
        translateTransition.setFromX(45);

        nodeHover.setOnMouseEntered(ev -> {
            translateTransition.setFromX(45);
            translateTransition.setByX(translateByX);
            translateTransition.play();
        });

        nodeHover.setOnMouseExited(ev -> {
            translateTransition.setDuration(Duration.millis(durationInMillis + 150));
            translateTransition.setFromX(55);
            translateTransition.setByX(-translateByX);
            translateTransition.play();
        });
    }

    public static void url(String url, Node node) {
        node.setOnMouseClicked(ev -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(Resources.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
