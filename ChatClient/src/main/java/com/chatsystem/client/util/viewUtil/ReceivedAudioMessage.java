package com.chatsystem.client.util.viewUtil;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class ReceivedAudioMessage extends AudioMessageView {

    public ReceivedAudioMessage(String text, String user, String date, String videoUrl) {
        super(text, user, date, videoUrl);
        setupSpecificLayout();
        setupSpecificStyling();
    }

    @Override
    protected void setupSpecificLayout() {
        // Set right margin for sent messages
        HBox.setMargin(this, new Insets(0, 0, 0, 100));
        this.getChildren().addAll(hSpacer, messageContainer);
    }

    @Override
    protected void setupSpecificStyling() {
        messageContainer.setStyle(messageContainer.getStyle() +
                "-fx-background-color: #838383ff;");
        footer.setStyle(footer.getStyle() + "-fx-background-color: #c2c2c2ff; -fx-background-radius: 20 0 20 20;");
        audioControls.setStyle(audioControls.getStyle() + "-fx-background-color:  #c2c2c2ff;");

    }

}
