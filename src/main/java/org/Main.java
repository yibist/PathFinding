package org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View.fxml"));
        loader.setController(new View());
        Parent root = loader.load();

        Scene scene = new Scene(root);
        URL cssUrl = getClass().getResource("/css/style.css");
        System.out.println("CSS URL: " + cssUrl);
        scene.getStylesheets().add(Objects.requireNonNull(cssUrl).toExternalForm());
        primaryStage.setTitle("Path Finding");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}