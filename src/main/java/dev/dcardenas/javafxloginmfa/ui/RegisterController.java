package dev.dcardenas.javafxloginmfa.ui;

import com.password4j.Password;
import dev.dcardenas.javafxloginmfa.db.DatabaseManager;
import dev.dcardenas.javafxloginmfa.security.InputValidator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private Button registerButton;

    @FXML
    private Button backToLoginButton;

    @FXML
    private Label registrationError;

    @FXML
    protected void attemptUserRegistration() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
    // 2025-06-15 18:41:45 ERROR d.d.j.ui.RegisterController - Error registering user:
    // [SQLITE_CONSTRAINT_NOTNULL] A NOT NULL constraint failed (NOT NULL constraint failed:
    // users.firstname)
    if (username.isEmpty() || password.isEmpty()) {
            registrationError.setText("Please fill in all fields.");
        } else {

            // Hash the password using Password4j
            String hashedPassword = Password.hash(password).withBcrypt().getResult();

            String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";

            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, hashedPassword);
                pstmt.executeUpdate();
                logger.info("User Registered: {}", username);
                ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/login-view.fxml"); // Go back to login after registration
            } catch (SQLException e) {
                logger.error("Error registering user: {}", e.getMessage());
                registrationError.setText("User registration failed, please try again.");
            }
            //password check?
        }
    }

    @FXML
    protected void backToLogin() {
        ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/login-view.fxml");
    }
}
