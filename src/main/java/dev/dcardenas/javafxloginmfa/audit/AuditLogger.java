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

import dev.dcardenas.javafxloginmfa.db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.dcardenas.javafxloginmfa.util.*;

public class AuditLogger {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);
    private static final String fingerprint = getSystemFingerprint();

    /**
     * Combines all the system fingerprint info into one string.
     */
    protected static String getSystemFingerprint() {
        return String.format("OS: %s, Java: %s, CPU Arch: %s, CPU Core Count: %s, Total Memory: %s, Default Encoding: %s,\n Default Locale: %s, Screen Resolution: %s, LocalIP: %s, MAC: %s, PublicIP: %s",
                SystemInfo.getOSName(),
                SystemInfo.getJavaVersion(),
                SystemInfo.getArchitecture(),
                SystemInfo.getCpuCoreCount(),
                SystemInfo.getTotalMemory(),
                SystemInfo.getDefaultEncoding(),
                SystemInfo.getDefaultLocale(),
                SystemInfo.getScreenResolution(),
                NetInfo.getLocalIpAddress(),
                NetInfo.getMacAddress(),
                NetInfo.getPublicIpAddress());
    }

    /**
     * Logs an action to the audit log with additional details required by NIST security controls.
     *
     * @param username The user performing the action.
     * @param action The action being logged.
     * @param eventType Type of event (e.g., LOGIN_SUCCESS, LOGIN_FAILURE, ACCOUNT_LOCKED, etc.).
     * fingerprint System fingerprint including IP address of the user.
     * @param outcome The outcome of the event (e.g., SUCCESS, FAILURE, ERROR).
     */
    public static void log(String username, String action, String eventType, String outcome) {
        String sql = "INSERT INTO audit_log (username, action, event_type, timestamp, outcome, fingerprint) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, action);
            stmt.setString(3, eventType);
            stmt.setTimestamp(4, Timestamp.from(Instant.now()));
            stmt.setString(5, outcome);
            stmt.setString(6, fingerprint);

            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error logging audit event: {}", e.getMessage());
        }
    }
}
