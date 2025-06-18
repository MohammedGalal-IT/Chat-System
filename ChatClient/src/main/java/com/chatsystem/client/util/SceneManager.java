package com.chatsystem.client.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SceneManager {

    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getStage(){
        return primaryStage;
    }
    public static void switchScene(String fxmlFile, String title) {
        try {
            Path viewPath = Paths.get(SceneManager.class.getResource("").toURI()).getParent().resolve("view/"+ fxmlFile);
            Parent root = FXMLLoader.load(viewPath.toUri().toURL());
            primaryStage.setTitle(title);
            primaryStage.setScene(new Scene(root));
            primaryStage.hide();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();    
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}