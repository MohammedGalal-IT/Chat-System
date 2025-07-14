package com.chatsystem.client.util.viewUtil;

import java.io.File;
import javafx.util.Duration;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public abstract class AudioMessageView extends HBox {
    protected MediaPlayer mediaPlayer;
    protected Text messageText;
    protected Label userLabel;
    protected Label dateLabel;
    protected VBox messageContainer;
    protected TextFlow textFlow;
    protected HBox footer;
    protected Region hSpacer;
    protected HBox audioControls;
    protected Button playPauseButton;
    protected Slider progressSlider;
    protected Label timeLabel;

    public AudioMessageView(String text, String user, String date, String audioUrl) {
        initializeComponents(text, user, date, audioUrl);
        setupLayout();
    }

    private void initializeComponents(String text, String user, String date, String audioUrl) {
        try {
            Media media = new Media(new File(audioUrl).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        } catch (Exception e) {
            System.err.println("Error loading audio: " + e.getMessage());
        }

        // Audio controls
        playPauseButton = new Button("▶");
        playPauseButton.setPrefWidth(40);
        playPauseButton.setStyle("-fx-background-radius: 20;");
        playPauseButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                MediaPlayer.Status status = mediaPlayer.getStatus();
                if (status == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                    playPauseButton.setText("▶");
                } else {
                    mediaPlayer.play();
                     playPauseButton.setText("❚❚");
                }
            }
        });

        progressSlider = new Slider();
        progressSlider.setMin(0);
        progressSlider.setMax(1);
        progressSlider.setValue(0);
        progressSlider.setPrefWidth(200);

        // Time Label
        timeLabel = new Label("00:00 / 00:00");
        timeLabel.setStyle("-fx-font-size: 10;");

        audioControls = new HBox(10, playPauseButton, progressSlider, timeLabel);
        audioControls.setAlignment(Pos.CENTER_LEFT);
        audioControls.setPadding(new Insets(0, 0, 0, 0));
        audioControls.setStyle("-fx-spacing: 10; -fx-alignment: CENTER_LEFT; -fx-background-color: #f3ebfa; -fx-background-radius: 8; -fx-padding: 3 10 3 10;");

        // Bind slider to mediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.setOnReady(() -> {
                progressSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
            });
            mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                if (!progressSlider.isValueChanging()) {
                    progressSlider.setValue(newTime.toSeconds());
                }
                updateTimeLabel(newTime, mediaPlayer.getTotalDuration());
            });
            progressSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
                if (!isChanging) {
                    mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
                }
            });

            // Allow seeking with slider
            progressSlider.setOnMousePressed(e -> {
                mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
            });
            
            progressSlider.setOnMouseDragged(e -> {
                mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
            });

            progressSlider.setOnMouseReleased(e -> {
                mediaPlayer.seek(Duration.seconds(progressSlider.getValue()));
            });
            mediaPlayer.setOnPlaying(() -> playPauseButton.setText("⏸"));
            mediaPlayer.setOnPaused(() -> playPauseButton.setText("▶"));
            mediaPlayer.setOnEndOfMedia(() -> {
                playPauseButton.setText("▶");
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.pause();
            });
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
        this.setPrefHeight(168.0);
        this.setPrefWidth(697.0);
        HBox.setMargin(this, new Insets(0, 0, 0, 0));

        // Create VBox (message container)
        messageContainer = new VBox();
        messageContainer.setAlignment(Pos.CENTER_LEFT);
        messageContainer.setPrefHeight(168.0);
        messageContainer.setPrefWidth(370.0);
        messageContainer.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-alignment: right;");
        messageContainer.setPadding(new Insets(15, 15, 10, 15));
        messageContainer.setSpacing(10);

        // Create text flow for message
        textFlow = new TextFlow();
        textFlow.setPrefHeight(32.0);
        textFlow.setPrefWidth(484.0);
        textFlow.getChildren().add(messageText);

        // Create footer HBox
        footer = new HBox();
        footer.setPrefHeight(18.0);
        footer.setPrefWidth(418.0);
        footer.setStyle("-fx-spacing: 10; -fx-background-color: #ffffff; -fx-padding: 2 10; -fx-background-radius: 20;");
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
        messageContainer.getChildren().addAll(audioControls, textFlow, footer);

        // Add spacer and message container to main HBox
        hSpacer = new Region();
        hSpacer.setPrefHeight(168.0);
        hSpacer.setPrefWidth(18.0);
        HBox.setHgrow(hSpacer, Priority.ALWAYS);

        // this.getChildren().addAll(messageContainer, hSpacer);
    }

    private void updateTimeLabel(Duration current, Duration total) {
        timeLabel.setText(
            formatTime(current) + " / " + formatTime(total)
        );
    }

    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) duration.toSeconds() % 60;
        return String.format("%02d:%02d", minutes, seconds);
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

    public void setAudio(Media media) {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }
        this.mediaPlayer = new MediaPlayer(media);
    }


}
