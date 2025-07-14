package com.chatsystem.client.util.viewUtil;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.awt.Desktop;

public abstract class VideoMessageView extends HBox {
    
    protected MediaView mediaView;
    protected StackPane mediaStack;
    protected ImageView playIconView;
    protected Text messageText;
    protected Label userLabel;
    protected Label dateLabel;
    protected VBox messageContainer;
    protected TextFlow textFlow;
    protected HBox footer;
    protected Region hSpacer;
    protected ImageView switchIconView;

    public VideoMessageView(String text, String user, String date, String videoUrl) {
        initializeComponents(text, user, date, videoUrl);
        setupLayout();
    }

    private void initializeComponents(String text, String user, String date, String videoUrl) {
        // Create the media view
        mediaView = new MediaView();
        mediaView.setFitHeight(300.0);
        mediaView.setFitWidth(635.0);
        mediaView.setPickOnBounds(true);
        mediaView.setPreserveRatio(true);

        // Create the play icon overlay
        playIconView = new ImageView();
        try {
            Image playIcon = new Image(getClass().getResource("/com/chatsystem/client/Assets/video_play_icon.png").toExternalForm());
            playIconView.setImage(playIcon);
            playIconView.setFitWidth(80);
            playIconView.setFitHeight(80);
            playIconView.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Error loading play icon: " + e.getMessage());
        }
        playIconView.setMouseTransparent(true); // Let clicks pass through to mediaView


        // Create the switch icon (top-right corner)
        switchIconView = new ImageView();
        try {
            Image switchIcon = new Image(getClass().getResource("/com/chatsystem/client/Assets/switch.png").toExternalForm());
            switchIconView.setImage(switchIcon);
            switchIconView.setFitWidth(32);
            switchIconView.setFitHeight(32);
            switchIconView.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Error loading switch icon: " + e.getMessage());
        }
        switchIconView.setVisible(false); // Initially hidden
        switchIconView.setStyle("-fx-cursor: hand;");
        StackPane.setAlignment(switchIconView, Pos.TOP_RIGHT);
        StackPane.setMargin(switchIconView, new Insets(8, 8, 0, 0));

        // StackPane to overlay play icon and switch icon on media view
        mediaStack = new StackPane(mediaView, playIconView, switchIconView);
        mediaStack.setAlignment(Pos.CENTER);
        mediaStack.setPrefSize(635, 300);

        try {
            Media media = new Media(new File(videoUrl).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            // Show/hide play icon and switch icon based on player state
            Runnable showPlayIcon = () -> {
                playIconView.setVisible(true);
                switchIconView.setVisible(false);
            };
            Runnable showSwitchIcon = () -> {
                playIconView.setVisible(false);
                switchIconView.setVisible(true);
            };
            mediaPlayer.setOnPlaying(showSwitchIcon);
            mediaPlayer.setOnPaused(showPlayIcon);
            mediaPlayer.setOnReady(showPlayIcon);
            mediaPlayer.setOnEndOfMedia(showPlayIcon);

            // Play/pause on click (except on switch icon)
            mediaStack.setOnMouseClicked(event -> {
                if (event.getTarget() == switchIconView) return;
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.play();
                }
            });

            // Open in system player when clicking switch icon
            switchIconView.setOnMouseClicked(event -> {
                try {
                    File file = new File(videoUrl);
                    if (file.exists()) {
                        mediaPlayer.pause(); // Stop the media player before opening
                        Desktop.getDesktop().open(file);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                event.consume();
            });
        } catch (Exception e) {
            System.err.println("Error loading video: " + e.getMessage());
        }

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
        messageContainer.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-alignment: right;"); // will be overridden
        messageContainer.setPadding(new Insets(15, 15, 10, 15));
        messageContainer.setSpacing(10);

        // Create text flow for message
        textFlow = new TextFlow();
        textFlow.setPrefHeight(192.0);
        textFlow.setPrefWidth(484.0);
        textFlow.getChildren().add(messageText);

        // Create footer HBox
        footer = new HBox();
        footer.setPrefHeight(18.0);
        footer.setPrefWidth(418.0);
        footer.setStyle("-fx-spacing: 10; -fx-background-color: #ffffff; -fx-padding: 2 10; -fx-background-radius: 20;"); // will be overridden
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
        messageContainer.getChildren().addAll(mediaStack, textFlow, footer);

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

    
}
