package com.chatsystem.client.util.testUtil;

import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ModernDialog extends Dialog<ButtonType> {

    public ModernDialog(String title, String message, AlertType alertType) {
        setTitle(title);
        setResizable(false);
        initStyle(StageStyle.UNDECORATED); // Removes window decorations

        VBox dialogBox = new VBox(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);"
                + "-fx-padding: 20px;"
                + "-fx-background-radius: 15px;"
                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 15, 0, 0, 5);");

        // 🖼 Dynamic Icon Selection
        ImageView icon = getIcon(alertType);
        
        Label dialogMessage = new Label(message);
        dialogMessage.setFont(Font.font("Arial", 16));
        dialogMessage.setTextFill(Color.DARKBLUE);

        // 🔘 Create Button Options
        ButtonType primaryButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType secondaryButton = alertType == AlertType.CONFIRMATION ? new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE) : null;

        getDialogPane().getButtonTypes().add(primaryButton);
        if (secondaryButton != null) getDialogPane().getButtonTypes().add(secondaryButton);

        HBox contentBox = new HBox(10, icon, dialogMessage);
        contentBox.setAlignment(Pos.CENTER);

        dialogBox.getChildren().add(contentBox);
        getDialogPane().setContent(dialogBox);

        // 🎬 Smooth Slide-in Animation
        animateDialog(dialogBox);
    }

    private ImageView getIcon(AlertType type) {
        String iconPath = switch (type) {
            case ERROR -> "file:error.png";
            case WARNING -> "file:warning.png";
            case INFORMATION -> "file:info.png";
            case CONFIRMATION -> "file:question.png";
            default -> "file:info.png";
        };
        ImageView icon = new ImageView(new Image(iconPath));
        icon.setFitWidth(30);
        icon.setFitHeight(30);
        return icon;
    }

    private void animateDialog(Node node) {
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(400), node);
        slideIn.setFromY(-50);
        slideIn.setToY(0);
        slideIn.play();
    }
}

