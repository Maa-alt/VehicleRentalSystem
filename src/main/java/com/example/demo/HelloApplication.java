package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Signup.fxml"));

        // Load the scene with the FXML
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        // Apply the CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("/Css/style.css").toExternalForm());

        // Set the title and scene to the stage
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
