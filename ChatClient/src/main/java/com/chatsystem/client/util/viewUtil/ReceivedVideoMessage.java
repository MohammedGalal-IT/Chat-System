package com.chatsystem.client.util.viewUtil;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;

public class ReceivedVideoMessage extends VideoMessageView {

    public ReceivedVideoMessage(String text, String user, String date, String videoUrl) {
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
                "-fx-background-color: #838383ff; -fx-background-radius: 20 0 20 20;");
        footer.setStyle(footer.getStyle() + "-fx-background-color: #c2c2c2ff;");
    }

}
