package dev.dcardenas.javafxloginmfa.ui;

import com.password4j.Password;
import dev.dcardenas.javafxloginmfa.db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
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
                System.out.println("User Registered: " + username);
                ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/login-view.fxml"); // Go back to login after registration
            } catch (SQLException e) {
                System.err.println("Error registering user: " + e.getMessage());
            }
            //password check?
        }
    }

    @FXML
    protected void backToLogin() {
        ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/login-view.fxml");
    }
}
