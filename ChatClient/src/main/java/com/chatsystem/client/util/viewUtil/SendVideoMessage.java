package com.chatsystem.client.util.viewUtil;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class SendVideoMessage extends VideoMessageView {

    public SendVideoMessage(String text, String user, String date, String audioUrl) {
        super(text, user, date, audioUrl);
        setupSpecificLayout();
        setupSpecificStyling();
    }

    @Override
    protected void setupSpecificLayout() {
        // Set right margin for sent messages
        HBox.setMargin(this, new Insets(0, 100, 0, 0)); 
        this.getChildren().addAll(messageContainer, hSpacer);
    }

    @Override
    protected void setupSpecificStyling() {
        messageContainer.setStyle(messageContainer.getStyle() + 
            "-fx-background-color: #d7c8e3;");
        footer.setStyle(footer.getStyle() + "-fx-background-color: #f3ebfa;");
    }
    
}
