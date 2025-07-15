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

public abstract class ImageMessageView extends HBox {

    protected ImageView imageView;
    protected Text messageText;
    protected Label userLabel;
    protected Label dateLabel;
    protected VBox messageContainer;
    protected TextFlow textFlow;
    protected HBox footer;
    protected Region hSpacer;

    public ImageMessageView(String text, String user, String date, String imageUrl) {
        initializeComponents(text, user, date, imageUrl);
        setupLayout();
    }

    private void initializeComponents(String text, String user, String date, String imageUrl) {
        // Create the image view
        imageView = new ImageView();
        imageView.setFitHeight(300.0);
        imageView.setFitWidth(635.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        try {
            Image image = new Image(new File(imageUrl).toURI().toString());
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }

        imageView.setOnMouseClicked(event -> {
            try {
                File file = new File(imageUrl); // imagePath should be the absolute path to the image file
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Create message text
        messageText = new Text(text);
        messageText.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        messageText.setStrokeWidth(0.0);
        messageText.setFont(Font.font("Cairo Regular", 12));

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
    }

    private void setupLayout() {
        // Main HBox settings
        this.setPrefHeight(388.0);
        this.setPrefWidth(697.0);
        HBox.setMargin(this, new Insets(0, 0, 0, 0)); // will be overridden

        // Create VBox (message container)
        messageContainer = new VBox();
        messageContainer.setAlignment(Pos.CENTER_LEFT);
        messageContainer.setPrefHeight(388.0);
        messageContainer.setPrefWidth(246.0);
        messageContainer.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-alignment: right;"); // will
                                                                                                                      // be
                                                                                                                      // overridden
        messageContainer.setPadding(new Insets(15, 15, 10, 15));
        messageContainer.setSpacing(10);

        messageText.setStyle("-fx-fill: white;");

        // Create text flow for message
        textFlow = new TextFlow();
        textFlow.setPrefHeight(192.0);
        textFlow.setPrefWidth(484.0);
        textFlow.getChildren().add(messageText);

        // Create footer HBox
        footer = new HBox();
        footer.setPrefHeight(18.0);
        footer.setPrefWidth(418.0);
        footer.setStyle(
                "-fx-spacing: 10; -fx-background-color: #ffffff; -fx-padding: 2 10; -fx-background-radius: 20;"); // will
                                                                                                                  // be
                                                                                                                  // overridden
        VBox.setVgrow(footer, Priority.ALWAYS);

        // Create date container
        HBox dateContainer = new HBox();
        dateContainer.setAlignment(Pos.CENTER_RIGHT);
        dateContainer.setPrefHeight(14.0);
        dateContainer.setPrefWidth(140.0);
        dateContainer.setStyle("-fx-background-radius: 20;");
        dateContainer.getChildren().add(dateLabel);

        // Add components to footer
        Region spacer = new Region();
        spacer.setPrefHeight(14.0);
        spacer.setPrefWidth(112.0);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        footer.getChildren().addAll(userLabel, spacer, dateContainer);

        // Add components to message container
        messageContainer.getChildren().addAll(footer, imageView, textFlow);

        // Add spacer and message container to main HBox
        hSpacer = new Region();
        hSpacer.setPrefHeight(388.0);
        hSpacer.setPrefWidth(18.0);
        HBox.setHgrow(hSpacer, Priority.ALWAYS);

        // this.getChildren().addAll(messageContainer, hSpacer);
    }

    protected abstract void setupSpecificLayout();

    protected abstract void setupSpecificStyling();

    // Getters and setters for dynamic properties
    public void setMessageText(String text) {
        this.messageText.setText(text);
    }

    public void setUser(String user) {
        this.userLabel.setText(user);
    }

    public void setDate(String date) {
        this.dateLabel.setText(date);
    }

    public void setImage(Image image) {
        this.imageView.setImage(image);
    }
}