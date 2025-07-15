package com.chatsystem.client.util.viewUtil;

import javafx.geometry.Pos;

public class ReceivedDocumentMessage extends DocumentMessageView {

    public ReceivedDocumentMessage(String text, String user, String date, String documentUrl) {
        super(text, user, date, documentUrl);
        setupSpecificLayout();
        applySpecificStyles();
    }

    @Override
    protected void setupSpecificLayout() {
        this.setAlignment(Pos.CENTER_RIGHT);
        this.getChildren().addAll(hSpacer, messageContainer);
    }

    @Override
    protected void applySpecificStyles() {
        messageContainer.setStyle("-fx-background-color: #838383ff; -fx-background-radius: 20 0 20 20; -fx-alignment: right;");
        headerBox.setStyle("-fx-background-color: #c2c2c2ff; -fx-padding: 2 10; -fx-background-radius: 20;");
        fileInfoBox.setStyle("-fx-background-color: #c2c2c2ff; -fx-background-radius: 10;");
    }
}
