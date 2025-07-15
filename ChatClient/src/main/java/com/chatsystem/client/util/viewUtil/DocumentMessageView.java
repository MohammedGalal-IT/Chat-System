package com.chatsystem.client.util.viewUtil;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.awt.Desktop;

public abstract class DocumentMessageView extends HBox{
    protected Label userLabel;
    protected Label dateLabel;
    protected Text messageText;
    protected ImageView fileIcon;
    protected Text fileNameText;
    protected VBox messageContainer;
    protected HBox headerBox;
    protected HBox fileInfoBox;
    protected Region hSpacer;
    protected String fileUrl;

    public DocumentMessageView(String text, String user, String date, String fileUrl) {
        initializeComponents(text, user, date, fileUrl);
        setupCommonLayout();

    }

    protected void initializeComponents(String text, String user, String date, String fileUrl) {
        // Create message text
        messageText = new Text(text);
        messageText.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        messageText.setStrokeWidth(0.0);
        messageText.setFont(Font.font("Cairo Regular", 12));
        messageText.setFill(javafx.scene.paint.Color.WHITE);

        // Create user label
        userLabel = new Label(user);
        userLabel.setPrefHeight(14.0);
        userLabel.setPrefWidth(154.0);
        userLabel.setFont(Font.font(9));
        

        // Create date label
        dateLabel = new Label(date);
        dateLabel.setAlignment(Pos.CENTER_RIGHT);
        dateLabel.setPrefHeight(14.0);
        dateLabel.setPrefWidth(142.0);
        dateLabel.setFont(Font.font(9));
        
        
         // Load file icon
         fileIcon = createFileIcon();

         // File name text
         File file = new File(fileUrl);
         fileNameText = new Text(file.getName());
        fileNameText.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        fileNameText.setStrokeWidth(0.0);
        fileNameText.setFont(Font.font("Cairo Regular", 12));

        // file URL
        this.fileUrl = fileUrl;

    }

    protected void setupCommonLayout() {
        // Create common container structure
        messageContainer = new VBox();
        messageContainer.setAlignment(Pos.CENTER_LEFT);
        messageContainer.setPrefHeight(121.0);
        messageContainer.setPadding(new Insets(15, 15, 10, 15));
        messageContainer.setSpacing(10);
        messageContainer.setStyle("-fx-background-color: #8A034b; -fx-background-radius: 10; -fx-alignment: right;");

        // Create common header
        headerBox = new HBox();
        headerBox.setPrefHeight(18.0);
        headerBox.setPrefWidth(476.0);
        headerBox.setSpacing(10);
        headerBox.setStyle("-fx-background-color: #f3ebfa; -fx-padding: 2 10; -fx-background-radius: 20;");
        VBox.setVgrow(headerBox, Priority.ALWAYS);

        // Create common date container
        HBox dateContainer = new HBox();
        dateContainer.setAlignment(Pos.CENTER_RIGHT);
        dateContainer.setPrefHeight(14.0);
        dateContainer.setPrefWidth(140.0);
        dateContainer.setStyle("-fx-background-radius: 20;");
        dateContainer.getChildren().add(dateLabel);

        // Add common header components
        Region headerSpacer = new Region();
        headerSpacer.setPrefHeight(200.0);
        headerSpacer.setPrefWidth(200.0);
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        headerBox.getChildren().addAll(userLabel, headerSpacer, dateContainer);

        // Create common file info box
        fileInfoBox = new HBox();
        fileInfoBox.setAlignment(Pos.CENTER_LEFT);
        fileInfoBox.setPadding(new Insets(5, 15, 5, 15));
        fileInfoBox.setSpacing(10);
        fileInfoBox.setStyle("-fx-background-color: #f3ebfa; -fx-background-radius: 10;");
        fileInfoBox.getChildren().addAll(fileIcon, fileNameText);
        fileInfoBox.setOnMouseClicked(event -> {
            try {
                File file = new File(fileUrl); // imagePath should be the absolute path to the image file
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Create common message flow
        TextFlow messageFlow = new TextFlow();
        messageFlow.setPrefHeight(13.0);
        messageFlow.setPrefWidth(484.0);
        messageFlow.setPadding(new Insets(0, 5, 0, 5));
        messageFlow.getChildren().add(messageText);

        // Add common components to container
        messageContainer.getChildren().addAll(headerBox, fileInfoBox, messageFlow);

        hSpacer = new Region();
        hSpacer.setPrefHeight(121.0);
        hSpacer.setPrefWidth(401.0);
        HBox.setHgrow(hSpacer, Priority.ALWAYS);
    }

    private ImageView createFileIcon() {
        ImageView icon = new ImageView();
        icon.setFitHeight(26.0);
        icon.setFitWidth(26.0);
        icon.setPickOnBounds(true);
        icon.setPreserveRatio(true);
        try {
            Image image = new Image(DocumentMessageView.class.getResource("/com/chatsystem/client/Assets/file_icon.png").toExternalForm());
            icon.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading file icon: " + e.getMessage());
        }
        return icon;
    }

    // Abstract methods for specific implementations
    protected abstract void setupSpecificLayout();
    protected abstract void applySpecificStyles();
    
}
