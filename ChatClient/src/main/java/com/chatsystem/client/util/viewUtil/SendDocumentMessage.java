package com.chatsystem.client.util.viewUtil;


public class SendDocumentMessage extends DocumentMessageView {

    public SendDocumentMessage(String text, String user, String date, String documentUrl) {
        super(text, user, date, documentUrl);
        setupSpecificLayout();
        applySpecificStyles();
    }

    @Override
    protected void setupSpecificLayout() {
        this.getChildren().addAll(messageContainer, hSpacer);
    }

    @Override
    protected void applySpecificStyles() {
        messageContainer.setStyle("-fx-background-color: #8A034b; -fx-background-radius: 0 20 20 20; -fx-alignment: right;");
        headerBox.setStyle("-fx-background-color: #f3ebfa; -fx-padding: 2 10; -fx-background-radius: 20;");
        fileInfoBox.setStyle("-fx-background-color: #f3ebfa; -fx-background-radius: 10;");
    }

    
    
}
