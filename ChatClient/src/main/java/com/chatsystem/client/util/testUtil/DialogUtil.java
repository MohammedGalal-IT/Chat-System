package com.chatsystem.client.util.testUtil;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DialogUtil {

    public static void showDialog(Stage parentStage) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(parentStage);

        VBox dialogBox = new VBox(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);"
                + "-fx-padding: 20px;"
                + "-fx-background-radius: 15px;"
                + "-fx-border-radius: 15px;"
                + "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.3), 15, 0, 0, 5);");

        Text title = new Text("Beautiful Web-style Dialog");
        title.setFont(Font.font("Arial", 20));
        title.setFill(Color.DARKBLUE);

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #1976D2;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 14px;"
                + "-fx-background-radius: 10px;");
        closeButton.setOnAction(e -> dialogStage.close());

        dialogBox.getChildren().addAll(title, closeButton);

        StackPane dialogPane = new StackPane(dialogBox);
        Scene dialogScene = new Scene(dialogPane, 300, 200);
        dialogStage.setScene(dialogScene);

        // 🎬 Smooth Fade-in Animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), dialogPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        dialogStage.show();
    }
    
}
