package com.chatsystem.client.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import com.chatsystem.client.network.Request;
import com.chatsystem.client.network.Action;
import com.chatsystem.client.Model.User;
import com.chatsystem.client.network.ClientSocketManager;
import com.chatsystem.client.network.Response;
import com.chatsystem.client.util.Session;
import com.chatsystem.client.util.testUtil.DialogUtil;
import com.chatsystem.client.util.testUtil.ModernDialog;
import com.chatsystem.client.util.viewUtil.SceneManager;
import com.dustinredmond.fxalert.FXAlert;
import com.google.gson.Gson;
import javafx.application.Platform;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    private ClientSocketManager clientSocketManager;

    @FXML
    public void initialize() {
        clientSocketManager = ClientSocketManager.getInstance();
        clientSocketManager.setOnResponse((response) -> {
            if (response.getAction() == Action.LOGIN) {
                if (response.isSuccess()) {
                    Platform.runLater(() -> {
                        showSuccess("Login successful");
                    });
                    Session.currentUser = response.getUser();
                } else {
                    Platform.runLater(() -> showError("Login failed: " + response.getMessage()));
                }
            }
        });
        clientSocketManager.startListening();
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Email and password are required.");
            return;
        }

        User user = new User(null, email, password);
        Request request = new Request();
        request.setAction(Action.LOGIN);
        request.setUser(user);
        clientSocketManager.sendRequest(request);
    }

    @FXML
    private void handleRegister() {
        SceneManager.switchScene("RegisterView.fxml", "Chat System - Register");
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setContentText(message);
        alert.showAndWait();

        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Login Success");
        alert.setContentText(message);
        alert.showAndWait();
        SceneManager.getStage().hide();
        SceneManager.switchScene("MainChatView.fxml", "Chat System");
    }
}
