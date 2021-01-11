package com.laynezcoder.estfx.alerts;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.laynezcoder.estfx.database.DatabaseHelper;
import com.laynezcoder.estfx.fonts.Fonts;
import com.laynezcoder.estfx.resources.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AlertsBuilder {

    private static String title;
    private static String buttonStyle;
    private static String titleStyle;
    private static String bodyStyle;
    private static JFXDialog dialog;

    public static void create(AlertType type, StackPane dialogContainer, Node nodeToBlur, Node nodeToDisable, String body) {
        setFunction(type);

        AnchorPane root = new AnchorPane();
        root.setPrefSize(390, 230);
        root.getStylesheets().add(Constants.LIGHT_THEME);

        JFXButton button = new JFXButton("Okey");
        button.getStyleClass().add(buttonStyle);
        Fonts.toButton(button, 15);

        HBox buttonContainer = new HBox();
        buttonContainer.setLayoutY(115);
        buttonContainer.setPrefSize(390, 115);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(button);

        Text textTitle = new Text(title);
        textTitle.getStyleClass().add(titleStyle);
        Fonts.toText(textTitle, 30);

        Text textBody = new Text(body);
        textBody.getStyleClass().add(bodyStyle);
        Fonts.toText(textBody, 15);

        VBox textContainer = new VBox();
        textContainer.setSpacing(5);
        textContainer.setPrefSize(390, 115);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        textContainer.setPadding(new Insets(0, 0, 0, 30));
        textContainer.getChildren().addAll(textTitle, textBody);
        root.getChildren().addAll(buttonContainer, textContainer);

        nodeToDisable.setDisable(true);
        nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);

        dialog = new JFXDialog();
        dialog.setContent(root);
        dialog.setDialogContainer(dialogContainer);
        dialog.setBackground(Background.EMPTY);
        dialog.setTransitionType(DatabaseHelper.dialogTransition());
        setStyle();
        dialog.show();

        button.setOnMouseClicked(e -> {
            dialog.close();
        });

        dialog.setOnDialogOpened(e -> {
            nodeToDisable.setDisable(true);
            nodeToBlur.setEffect(Constants.BOX_BLUR_EFFECT);
        });

        dialog.setOnDialogClosed(e -> {
            nodeToDisable.setDisable(false);
            nodeToBlur.setEffect(null);
        });
    }
    
    public static void close() {
        if (dialog != null) {
            dialog.close();
        }
    }

    private static void setStyle() {
        dialog.getStylesheets().add(Constants.LIGHT_THEME);
        dialog.getStyleClass().add("jfx-dialog-overlay-pane");
    }

    private static void setFunction(AlertType type) {
        switch (type) {
            case SUCCES:
                title = "Succes!";
                buttonStyle = "button-alert-success";
                titleStyle = "title-alert-success";
                bodyStyle = "body-alert-success";
                break;
            case ERROR:
                title = "Oops!";
                buttonStyle = "button-alert-error";
                titleStyle = "title-alert-error";
                bodyStyle = "body-alert-error";
                break;
        }
    }
}
