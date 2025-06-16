package dev.dcardenas.javafxloginmfa;

import dev.dcardenas.javafxloginmfa.audit.AuditLogReader;
import dev.dcardenas.javafxloginmfa.audit.AuditLogger;
import dev.dcardenas.javafxloginmfa.db.DatabaseManager;
import dev.dcardenas.javafxloginmfa.ui.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.ensureDatabaseSetup();
        //System.out.println(AuditLogger.getSystemFingerprint());
        //DatabaseManager.helperWipeDatabase();
        //AuditLogReader.readAllAuditLogs();

        ViewManager.setStage(primaryStage);
        ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/login-view.fxml");
        primaryStage.setTitle("JavaFX App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
