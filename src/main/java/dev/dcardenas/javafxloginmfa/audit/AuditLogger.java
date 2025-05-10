package dev.dcardenas.javafxloginmfa.audit;
/**
 * Log authentication failures and lockouts.
 * AU-3 - CONTENT OF AUDIT RECORDS
 * Control: Ensure that audit records contain information that establishes the following:
 * a. What type of event occurred;
 * b. When the event occurred;
 * c. Where the event occurred;
 * d. Source of the event;
 * e. Outcome of the event; and
 * f. Identity of any individuals, subjects, or objects/entities associated with the event.
 */
// File: AuditLogger.java

import dev.dcardenas.javafxloginmfa.db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class AuditLogger {
    /**
     * Logs an action to the audit log with additional details required by NIST security controls.
     *
     * @param username The user performing the action.
     * @param action The action being logged.
     * @param eventType Type of event (e.g., LOGIN_SUCCESS, LOGIN_FAILURE, ACCOUNT_LOCKED, etc.).
     * @param sourceIp The IP address of the user.
     * @param outcome The outcome of the event (e.g., SUCCESS, FAILURE, ERROR).
     */
    public static void log(String username, String action, String eventType, String sourceIp, String outcome) {
        String sql = "INSERT INTO audit_log (username, action, event_type, timestamp, source_ip, outcome) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, action);
            stmt.setString(3, eventType);
            stmt.setTimestamp(4, Timestamp.from(Instant.now()));
            stmt.setString(5, sourceIp);
            stmt.setString(6, outcome);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logging audit event: " + e.getMessage());
        }
    }
}
