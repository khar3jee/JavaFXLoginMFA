package dev.dcardenas.javafxloginmfa.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ViewManager {
    private static final Logger logger = LoggerFactory.getLogger(ViewManager.class);
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
            logger.error("Error switching view: {}", e.getMessage());
        }
    }
}

