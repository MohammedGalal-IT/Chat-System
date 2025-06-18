package com.chatsystem.client.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("view/ClientMain.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        ClientMain.showErrorAlert("Error", "Errror Message", "sorry you have to ....", stage);
    }

    public static void showErrorAlert(String title, String headerText, String contentText, Stage stage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Network Connection Failed"
               + "Failed to connect to the server.");
//        alert.setContentText(contentText);

        // Optional: Customize the owner window if you have a primary stage
         alert.initOwner(stage);

        alert.showAndWait(); // showAndWait makes the alert modal
    }

    public static void main(String[] args) {
        launch();
    }
}