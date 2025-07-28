package dev.dcardenas.javafxloginmfa.ui;

/**
 * Ensure login logic defers authentication to AuthenticationManager.
 * Display error messages when an account is locked.
 */
import dev.dcardenas.javafxloginmfa.security.AuthenticationManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.commons.lang3.Validate.notBlank;


public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public Button login;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    //private Label welcomeText;
    private Label loginError;

    @FXML
    protected void attemptLogIn() {
        String username = "";
        String password = "";
        try{
            username = notBlank(usernameField.getText().trim(), "Need Username");
            password = notBlank(passwordField.getText().trim(), "Need Password");
            System.out.println("Attempting to login user " + username + " with password " + password);
        } catch (IllegalArgumentException | NullPointerException e) {
            loginError.setText("Username and password are required to login");
            logger.error("getText field is null {}", e.getMessage());
            //add ip to log
        }

        if (AuthenticationManager.authenticate(username, password)) {
            //add source ip
            ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/app-view.fxml");
        } else {
            //add the why
            loginError.setText("Login Failed!");
        }
    }

    @FXML
    // register users
    protected void registerUser() {
        ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/register-view.fxml");
    }
}
/**
 * Should probably setup a background thread with a service to check for user inactivity and log them out after a timeout period.
 */
