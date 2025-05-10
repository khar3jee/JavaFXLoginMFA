package dev.dcardenas.javafxloginmfa.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewManager {
    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchView(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(ViewManager.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Error switching view: " + e.getMessage());
        }
    }
}

