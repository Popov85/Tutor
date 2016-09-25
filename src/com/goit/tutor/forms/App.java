package com.goit.tutor.forms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Андрей on 01.09.2016.
 */
public class App extends Application {

        public static void main(String[] args) {
                launch(args);
        }

        @Override
        public void start(Stage primaryStage) throws IOException {
                Parent root = FXMLLoader.load(getClass().getResource("Start.fxml"));
                primaryStage.setTitle("English Tutor");
                primaryStage.setScene(new Scene(root, 655, 318));
                primaryStage.setResizable(false);
                primaryStage.show();
        }
}
