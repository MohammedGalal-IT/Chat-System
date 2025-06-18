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
import com.chatsystem.client.util.DialogUtil;
import com.chatsystem.client.util.ModernDialog;
import com.chatsystem.client.util.SceneManager;
import com.dustinredmond.fxalert.FXAlert;
import com.google.gson.Gson;
import javafx.application.Platform;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    private ClientSocketManager clientSocketManager;

    @FXML
    public void initialize() {
        clientSocketManager = ClientSocketManager.getInstance();
        clientSocketManager.setOnResponse((response, fileData) -> {
            if(response != null) System.out.println("Request Received");
            if (response.getAction() == Action.REGISTER) {
                if (response.isSuccess()) {
                     Platform.runLater(() -> showSuccess("Registered successfully! You can now log in."));
                } else {
                    Platform.runLater(() -> showError("Register failed: " + response.getMessage()));
                }
            }
        });
        clientSocketManager.startListening();
    }

    @FXML
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        User user = new User(name, email, password);
        Request request = new Request();
        request.setAction(Action.REGISTER);
        request.setUser(user);
        clientSocketManager.sendRequest(request);
    }

    @FXML
    private void goToLogin() {
        SceneManager.switchScene("LoginView.fxml", "Chat System - Login");
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setContentText(message);
        alert.showAndWait();

        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Register Success");
        alert.setContentText(message);
        alert.showAndWait();
        this.goToLogin();
    }
}
