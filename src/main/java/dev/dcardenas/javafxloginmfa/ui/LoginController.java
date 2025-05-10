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

public class LoginController {
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
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        //add source ip

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
