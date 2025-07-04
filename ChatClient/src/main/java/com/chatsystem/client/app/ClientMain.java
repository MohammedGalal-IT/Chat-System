package com.chatsystem.client.app;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Path viewPath = Paths.get(ClientMain.class.getResource("").toURI()).getParent().resolve("view/LoginView.fxml");
            Path assetsPath =  Paths.get(ClientMain.class.getResource("").toURI()).getParent().resolve("Assets/chat_icon.png");

            FXMLLoader loader = new FXMLLoader(viewPath.toUri().toURL()); //ClientMain.class.getResource("view/loginView.fxml")
            Scene scene = new Scene(loader.load());

            primaryStage.setTitle("Chat Client - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image(assetsPath.toUri().toURL().toExternalForm()));
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load LoginView: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

