package dev.dcardenas.javafxloginmfa.ui;

import com.password4j.Password;
import dev.dcardenas.javafxloginmfa.db.DatabaseManager;
import dev.dcardenas.javafxloginmfa.security.InputValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dev.dcardenas.javafxloginmfa.user.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);


    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField firstnameField;

    @FXML
    private TextField lastnameField;

    @FXML
    private TextField emailAddressField;

    @FXML
    private Button registerUserButton;

    @FXML
    private Button backToLoginButton;

    @FXML
    private Label registrationError;

    private User user;

    @FXML
    protected void attemptUserRegistration() {
        try{
            this.user = new User(
                    new UserId(), //generates UUID internally
                    new Username(usernameField.getText()),
                    new UserPassword(passwordField.getText()), //12@asfleF345
                    new FirstName(firstnameField.getText()),
                    new LastName(lastnameField.getText()),
                    new EmailAddress(emailAddressField.getText())
            );
        } catch (IllegalArgumentException | NullPointerException e) {
            registrationError.setText(e.getMessage());
        } try { DatabaseManager.saveUser(this.user);
            logger.info("User Registered: {}", this.user.getUsername().value());
            ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/login-view.fxml");
                //PauseTransition pause = new PauseTransition(Duration.seconds(2));
                //pause.setOnFinished(event -> redirectToLogin());
                //pause.play();
            //} // Go back to login after registration
        } catch (SQLException e) {
                logger.error("Error registering user: {}", e.getMessage());
                registrationError.setText("User registration failed, please try again.");
        }

    }

    @FXML
    protected void backToLogin() {
        ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/login-view.fxml");
    }
}
