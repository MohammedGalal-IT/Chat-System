package com.chatsystem.client.util.viewUtil;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class ReceivedMessage extends MessageView {

    public ReceivedMessage(String text, String user, String date) {
        super(text, user, date);
    }

    @Override
    protected void customize() {
        this.setStyle("-fx-background-color: #838383ff; -fx-background-radius: 20 0 20 20; -fx-alignment: right;");
        VBox.setMargin(this, new Insets(0, 0, 0, 100));
        this.messageFooter.setStyle(
                "-fx-spacing: 20; -fx-background-color: #c2c2c2ff; -fx-padding: 2 10; -fx-background-radius: 5;");
    }

}
