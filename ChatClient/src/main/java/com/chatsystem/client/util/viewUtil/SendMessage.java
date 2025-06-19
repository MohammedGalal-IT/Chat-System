package com.chatsystem.client.util.viewUtil;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class SendMessage extends MessageView{

    public SendMessage(String text, String user, String date) {
        super(text, user, date);
        //TODO Auto-generated constructor stub
    }

    @Override
    protected void customize() {
        this.setStyle("-fx-background-color: #d7c8e3; -fx-background-radius: 10; -fx-alignment: right;");
        VBox.setMargin(this, new Insets(0, 100, 0, 0));
        this.messageFooter.setStyle("-fx-spacing: 10; -fx-background-color: #f3ebfa; -fx-padding: 2 10; -fx-background-radius: 20;");
    }
    
}
