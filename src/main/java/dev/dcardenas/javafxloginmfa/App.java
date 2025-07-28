package dev.dcardenas.javafxloginmfa;

import dev.dcardenas.javafxloginmfa.audit.AuditLogReader;
import dev.dcardenas.javafxloginmfa.audit.AuditLogger;
import dev.dcardenas.javafxloginmfa.db.DatabaseManager;
import dev.dcardenas.javafxloginmfa.security.AuthenticationManager;
import dev.dcardenas.javafxloginmfa.security.PepperGenerator;
import dev.dcardenas.javafxloginmfa.ui.ViewManager;
import dev.dcardenas.javafxloginmfa.user.*;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dave Cardenas
 * @version 0.1.0
 * App = launches the app, which calls ViewManager

 * audit
 * AuditLogger = audits user actions across the app like that ex that never leaves you alone.

 * db
 * DatabaseManager = initializes and manages db interactions

 * security
 * AuthenticationManager = gatekeeps the app's access to whoever's on the naughty list
 * InputValidator = validates input, excises naughty characters.

 * ui
 * ViewManager = changes views dynamically as called
 * LoginController = interfaces between FXML ui and login functions
 * RegisterController = interfaces between FXML ui and app registration functions
 * AppController = interfaces between FXML ui and app functions

 * util
 * NetInfo = collects your Ip and MAC addresses, totally won't be used to lock you out if you're bruteforcing.
 * Platform = attempts to identify your operating system in broad strokes, may have a stroke if not a common monolithic OS.
 * SystemInfo = collects system data points to fingerprint you ... definitely won't be used with NetInfo to lock you out.
 */

public class App extends Application {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.ensureDatabaseSetup();
        //System.out.println(AuditLogger.getSystemFingerprint());
        //DatabaseManager.helperWipeDatabase();
        //System.out.println(PepperGenerator.generateSecureString(48));
        //AuditLogReader.readAllAuditLogs();
        //AuthenticationManager.adminMenu(4,"khos1");
        //AuthenticationManager.adminMenu(3,"khos1");//UserId userId = new UserId();
        //System.out.println(userId.value());
        //Username username = new Username("Beto5-|9");
        //FirstName fn = new FirstName("Jo");
        //LastName ln = new LastName("D");
        //EmailAddress email = new EmailAddress("man+jo_o@gmail.com");
        //UserPassword userPassword = new UserPassword("P@ssword1234%");
    // System.out.println(fn.value()+ ln.value());
    // System.out.println(email.value());
        /**User user = new User(
                new UserId(),
                new Username("Dan666"),
                new UserPassword("12@asfleF345"),
                new FirstName("Dan"),
                new LastName("Dingles"),
                new EmailAddress("man+danoh@gmail.com"));

        **/
        ViewManager.setStage(primaryStage);
        ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/login-view.fxml");
        primaryStage.setTitle("JavaFX App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
