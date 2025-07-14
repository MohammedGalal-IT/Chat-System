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
            "-fx-background-color: #e5f280;");
        footer.setStyle(footer.getStyle() + "-fx-background-color: #f0faa7;");
        audioControls.setStyle(audioControls.getStyle() + "-fx-background-color:  #f0faa7;");

    }
    
}
