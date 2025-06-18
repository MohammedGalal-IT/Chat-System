package com.chatsystem.client;


import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.chatsystem.client.util.SceneManager;

public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            SceneManager.setStage(primaryStage);
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image(ClientMain.class.getResource("Assets/chat_icon.png").toExternalForm()));
            SceneManager.switchScene("LoginView.fxml", "Chat System - Login");
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Failed to load LoginView: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

