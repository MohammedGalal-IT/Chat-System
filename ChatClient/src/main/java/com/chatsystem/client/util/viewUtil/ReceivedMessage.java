package com.chatsystem.client.util.viewUtil;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class ReceivedMessage extends MessageView {

    public ReceivedMessage(String text, String user, String date) {
        super(text, user, date);
    }

    @Override
    protected void customize() {
        this.setStyle("-fx-background-color: #e5f280; -fx-background-radius: 10; -fx-alignment: right;");
        VBox.setMargin(this, new Insets(0, 0, 0, 100));
        this.messageFooter.setStyle("-fx-spacing: 20; -fx-background-color: #f0faa7; -fx-padding: 2 10; -fx-background-radius: 20;");
    }
    
}
