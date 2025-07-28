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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.dcardenas.javafxloginmfa.util.NetInfo;

import dev.dcardenas.javafxloginmfa.user.User;
import dev.dcardenas.javafxloginmfa.user.UserPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.commons.lang3.Validate.*;
import dev.dcardenas.javafxloginmfa.util.NetInfo;

public class AuthenticationManager {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int LOCKOUT_DURATION_MINUTES = 15;
    private static final String PEPPER_ENV_NAME = "APP_PEPPER";

    public static void adminMenu(int option, String input){
        switch (option){
            case 1:
                getFailedAttempts(input);
                break;
            case 2:
                lockAccount(input);
                break;
            case 3:
                isAccountLocked(input);
                break;
            case 4:
                resetFailedAttempts(input);
                break;
        }


    }

  /**
     * @param username
     * @param password
     * @return
     */

    public static boolean authenticate(String username, String password) {
        //String sourceIp = "127.0.0.1";
        if (isAccountLocked(username)) {
            AuditLogger.log(username, "Authentication attempt", "LOGIN_FAILURE", "ACCOUNT_LOCKED");
            return false;
        }

        boolean isAuthenticated = checkCredentials(username, password);
        if (isAuthenticated) {
            //System.out.println("User Authenticated");
            resetFailedAttempts(username);
            AuditLogger.log(username, "User authenticated", "LOGIN_SUCCESS", "SUCCESS");
            return true;
        } else {
            //System.out.println("User Not Authenticated");
            incrementFailedAttempts(username);
            AuditLogger.log(username, "User attempted authentication", "LOGIN_FAILURE", "FAILED");
            return false;
        }
    }

    private static boolean checkCredentials(String username, String password) {
        String sql = "SELECT password_hash, salt FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String storedSalt = rs.getString("salt");
                UserPassword userPw = new UserPassword(storedHash, storedSalt);
                return userPw.matches(password);

                //return com.password4j.Password.check(password, storedHash)
                 //       .addPepper(getPepper())
                   //     .addSalt(storedSalt)
                     //   .withArgon2();
            }
        } catch (SQLException e) {
            logger.error("Credential error for user '{}'", username, e);
        }
        return false;
    }

    private static void incrementFailedAttempts(String username) {
        String sql = "UPDATE users SET failed_attempts = failed_attempts + 1 WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.executeUpdate();

            int attempts = getFailedAttempts(username);
            if (attempts >= MAX_FAILED_ATTEMPTS) {
                lockAccount(username);
            }
        } catch (SQLException e) {
            logger.error("Increment failed login attempts error for user '{}'", username, e);
        }
        AuditLogger.log(username, "Failed login attempt", "LOGIN_FAILURE", "FAILURE");
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
            logger.error("Get failed login attempts error for user '{}'", username, e);
        }
        return 0;
    }


    private static void lockAccount(String username) {
        String sql = "UPDATE users SET locked_until = ? WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Account lockout error for user '{}'", username, e);
        }
        AuditLogger.log(username, "Account locked due to multiple failed logins", "ACCOUNT_LOCKED", "LOCKED");
    }

    private static boolean isAccountLocked(String username) {
        String sql = "SELECT locked_until FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            //System.out.println("AcctLock1");
            if (rs.next()) {
                LocalDateTime lockedUntil = rs.getObject("locked_until", LocalDateTime.class);
                try {
                    notNull(lockedUntil);
                } catch (NullPointerException e) {
                    //System.out.println("locked until is null");
                    return false;
                }
                //System.out.println("Locked: "+ lockedUntil);
                return true;
            } else {
                //System.out.println("AcctLock False");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Account lockout check error for user '{}'", username, e);
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
            logger.error("Account lockout reset error for user '{}'", username, e);
        }
    }

    public static String getPepper() {
        final String pepper = System.getenv(PEPPER_ENV_NAME);
        notNull(pepper);
        logger.info("Pepper called");
        return pepper;
    }
}
