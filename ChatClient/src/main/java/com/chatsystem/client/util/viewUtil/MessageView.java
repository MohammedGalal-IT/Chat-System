package com.chatsystem.client.util.viewUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public abstract class MessageView extends VBox {
    private final Text messageText;
    private final Label userLabel;
    private final Label dateLabel;
    protected HBox messageFooter;

    public MessageView(String text, String user, String date) {
        // تهيئة العناصر كما في الكود السابق
        this.messageText = new Text(text);
        this.userLabel = new Label(user);
        this.dateLabel = new Label(date);
        this.messageFooter = new HBox();

        // بناء الواجهة
        buildUI();
        customize();
    }

    protected void buildUI() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPrefHeight(78.0);
        this.setPrefWidth(705.0);
        this.setStyle(
                "-fx-background-color:  #838383ff; -fx-background-radius: 5; -fx-alignment: right; -fx-text-fill: #ffffff;");
        this.setPadding(new Insets(15, 15, 10, 15));
        this.setSpacing(10);
        VBox.setMargin(this, new Insets(0, 0, 0, 100));

        // إنشاء محتوى الرسالة (TextFlow)
        TextFlow messageContent = new TextFlow();
        messageContent.setPrefHeight(192.0);
        messageContent.setPrefWidth(484.0);

        messageText.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        messageText.setStrokeWidth(0.0);
        messageText.setFont(Font.font("Cairo Regular", 16));
        messageText.setStyle("-fx-fill: white;");

        messageContent.getChildren().add(messageText);

        // إنشاء تذييل الرسالة (HBox)
        this.messageFooter.setPrefHeight(25);
        this.messageFooter.setPrefWidth(475);
        this.messageFooter.setStyle(
                "-fx-spacing: 20; -fx-background-color: #c2c2c2ff; -fx-padding: 2 10; -fx-background-radius: 5;");

        // اسم المستخدم
        userLabel.setPrefHeight(25.0);
        userLabel.setPrefWidth(214.0);
        userLabel.setFont(Font.font(9));

        // منطقة فارغة للتوسيع
        Region spacer = new Region();
        spacer.setPrefHeight(200.0);
        spacer.setPrefWidth(200.0);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // تاريخ الرسالة
        HBox dateContainer = new HBox();
        dateContainer.setAlignment(Pos.CENTER_RIGHT);
        dateContainer.setPrefHeight(14.0);
        dateContainer.setPrefWidth(233.0);
        dateContainer.setStyle("-fx-background-radius: 20;");

        dateLabel.setAlignment(Pos.CENTER_RIGHT);
        dateLabel.setPrefHeight(14.0);
        dateLabel.setPrefWidth(221.0);
        dateLabel.setFont(Font.font(9));

        dateContainer.getChildren().add(dateLabel);

        // تجميع تذييل الرسالة
        messageFooter.getChildren().addAll(userLabel, spacer, dateContainer);

        // تجميع العناصر الرئيسية
        this.getChildren().setAll(messageFooter, messageContent);

    }

    protected abstract void customize(); // colors and margins

    public void setMessageText(String text) {
        this.messageText.setText(text);
    }

    public void setUser(String user) {
        this.userLabel.setText(user);
    }

    public void setDate(String date) {
        this.dateLabel.setText(date);
    }

}
