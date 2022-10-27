package com.mp3.mp3player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MyApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("application-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        //setting name and logo for our mp3 player
        stage.setTitle("MP3 Player");
        Image icon = new Image(String.valueOf(getClass().getResource("mp3logo.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }
}