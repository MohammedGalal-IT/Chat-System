package com.chatsystem.client.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClientMainController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}