package com.chatsystem.client.util.viewUtil;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class SendImageMessage extends ImageMessageView {

    public SendImageMessage(String text, String user, String date, String imageUrl) {
        super(text, user, date, imageUrl);
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
                "-fx-background-color: #8A034b; -fx-background-radius: 0 20 20 20;");
        footer.setStyle(footer.getStyle() + "-fx-background-color: #f3ebfa;");
    }

}
