package dev.dcardenas.javafxloginmfa.security;

/*
  Handle login attempts, authentication, and account lockout for AC-7.
  Verify user roles for privileged accounts (IA-2(1)).
  Integrate with DatabaseManager to store and check failed attempts.
 */
import dev.dcardenas.javafxloginmfa.audit.AuditLogger;
import dev.dcardenas.javafxloginmfa.db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AuthenticationManager {
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCKOUT_DURATION_MINUTES = 15;

    public static boolean authenticate(String username, String password) {
        return authenticate(username, password);
    }

    public static boolean authenticate(String username, String password, String sourceIp) {
        if (isAccountLocked(username)) {
            AuditLogger.log(username, "Authentication attempt", "LOGIN_FAILURE", sourceIp, "ACCOUNT_LOCKED");
            return false;
        }

        boolean isAuthenticated = checkCredentials(username, password);
        if (isAuthenticated) {
            resetFailedAttempts(username);
            AuditLogger.log(username, "User authenticated", "LOGIN_SUCCESS", sourceIp, "SUCCESS");
            return true;
        } else {
            incrementFailedAttempts(username, sourceIp);
            return false;
        }
    }

    private static boolean checkCredentials(String username, String password) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                return com.password4j.Password.check(password, storedHash).withBcrypt();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void incrementFailedAttempts(String username, String sourceIp) {
        String sql = "UPDATE users SET failed_attempts = failed_attempts + 1 WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();

            int attempts = getFailedAttempts(username);
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                lockAccount(username, sourceIp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogger.log(username, "Failed login attempt", "LOGIN_FAILURE", sourceIp, "FAILURE");
    }

    private static int getFailedAttempts(String username) {
        String sql = "SELECT failed_attempts FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("failed_attempts");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void lockAccount(String username, String sourceIp) {
        String sql = "UPDATE users SET locked_until = ? WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AuditLogger.log(username, "Account locked due to multiple failed logins", "ACCOUNT_LOCKED", sourceIp, "LOCKED");
    }

    private static boolean isAccountLocked(String username) {
        String sql = "SELECT locked_until FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                LocalDateTime lockedUntil = rs.getObject("locked_until", LocalDateTime.class);
                return lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void resetFailedAttempts(String username) {
        String sql = "UPDATE users SET failed_attempts = 0, locked_until = NULL WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
