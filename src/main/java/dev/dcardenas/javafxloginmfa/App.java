package dev.dcardenas.javafxloginmfa;

import dev.dcardenas.javafxloginmfa.db.DatabaseManager;
import dev.dcardenas.javafxloginmfa.ui.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Dave Cardenas
 * @version 0.0.9
 * App = launches app calls ViewManager
 * ViewManager = changes views dynamically as called
 * DatabaseManager = initializes and manages db interactions
 * LoginController = interfaces between FXML ui and login functions
 * RegisterController = interfaces between FXML ui and app registration functions
 * AppController = interfaces between FXML ui and app functions
 */

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.ensureDatabaseSetup();

        ViewManager.setStage(primaryStage);
        ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/login-view.fxml");
        primaryStage.setTitle("JavaFX App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
