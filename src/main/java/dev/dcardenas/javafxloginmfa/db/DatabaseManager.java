package dev.dcardenas.javafxloginmfa.db;
/**
 * Store failed login attempts and lockout status.
 */

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import dev.dcardenas.javafxloginmfa.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DatabaseManager manages the database, adds/removes entities, tables and updates
 */
public class DatabaseManager {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static final String DB_PATH = Paths.get(System.getProperty("user.dir"), "db", "javafxlogin.sqlite").toString();
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static boolean databaseInitialized = false; // Prevent multiple inits
    //private final Connection connection;

    //public DatabaseManager(Connection connection) {
    //    this.connection = connection;
        //DriverManager.getConnection(DB_URL)
    //}

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            logger.error("Database connection failed (url: {})", DB_URL, e);
            throw new RuntimeException("Database connection failed because of ", e);
        }
    }

    public static void helperWipeDatabase() {
        // helper method to drop tables and delete database files as needed while testing
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            //stmt.execute("DROP TABLE users CASCADE CONSTRAINTS;");
            //stmt.execute("DROP TABLE audit_log CASCADE CONSTRAINTS;");
            stmt.executeUpdate("DROP TABLE IF EXISTS users");
            stmt.executeUpdate("DROP TABLE IF EXISTS audit_log");
        } catch (SQLException e) {
            logger.error("issue attempting to drop tables {}", e.getMessage());
            throw new RuntimeException(e);
        }
        //delete db file?
    }
    public static void saveUser(User user) throws SQLException {
        String sql = "INSERT INTO users (user_id, username, password_hash, salt, firstname, lastname, email_address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, user.getUserId().value());
            pstmt.setObject(2, user.getUsername().value());
            pstmt.setObject(3, user.getPassword().value());
            pstmt.setObject(4, user.getSalt());
            pstmt.setObject(5, user.getFirstName().value());
            pstmt.setObject(6, user.getLastName().value());
            pstmt.setObject(7, user.getEmailAddress().value());
            pstmt.executeUpdate();
            } catch (SQLException e) {
            logger.error("issue attempting to save user {}", user, e);
        }
    }

    public static void ensureDatabaseSetup() {
        if (databaseInitialized) {
            logger.info("Database already initialized. Skipping setup.");
            return;
        }
        initializeDatabase();
        databaseInitialized = true;
    }

    private static void initializeDatabase() {
        ensureDbDirectoryExists();
        if (!okayDatabaseFile()){
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {

                if (!doesTableExist("users")) {
                    stmt.execute(
                            """
                                    CREATE TABLE users (
                                        user_id TEXT PRIMARY KEY,
                                        username TEXT UNIQUE NOT NULL,
                                        password_hash TEXT NOT NULL,
                                        salt TEXT NOT NULL,
                                        firstname TEXT NOT NULL,
                                        lastname TEXT NOT NULL,
                                        email_address TEXT UNIQUE NOT NULL,
                                        failed_attempts INTEGER DEFAULT 0,
                                        locked_until DATETIME NULL,
                                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                                    );
                                """);
                    logger.info("Users table created.");
                } else {
                    logger.info("Users table already exists.");
                }

                if (!doesTableExist("audit_log")) {
                    stmt.execute("""
                    CREATE TABLE audit_log (
                        user_id TEXT PRIMARY KEY,
                        username TEXT,
                        action TEXT NOT NULL,
                        event_type TEXT NOT NULL,
                        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                        outcome TEXT NOT NULL,
                        fingerprint TEXT
                    );
                """);
                    logger.info("Audit log table created.");
                } else {
                    logger.info("Audit log table already exists.");
                }
                logger.info("Database initialized successfully.");
            } catch (SQLException e) {
                logger.error("Error initializing database: {}", e.getMessage());
                throw new RuntimeException("Failed to initialize database", e);
            }
        }
    }

    private static void ensureDbDirectoryExists() {
        File dbDir = new File(System.getProperty("user.dir") + "/db/");
        if (!dbDir.exists()) {
            boolean created = dbDir.mkdirs();
            if (created) {
                logger.info("Created missing directory at: {}", dbDir.getAbsolutePath());
            } else {
                logger.error("Failed to create database directory at path: {}", dbDir.getAbsolutePath());
            }
        }
    }

    private static boolean okayDatabaseFile() {
        File dbFile = new File(DB_PATH);
        boolean answer = true;

        if (!dbFile.exists()) {
            logger.error("Database file not found at: {}", dbFile.getAbsolutePath());
            answer = false;
        } else {
            logger.info("Database found at: {}", dbFile.getAbsolutePath());

            if (!doesTableExist("users")) {
                logger.error("WARNING: Database file exists, but '{}' table is missing!", "users");
                answer = false;
            }
            if (!doesTableExist("audit_log")) {
                logger.error("WARNING: Database file exists, but '{}' table is missing!", "audit_log");
                answer = false;
            }

        }
        return answer;
    }

    private static boolean doesTableExist(String tableName) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';")) {
            return rs.next();
        } catch (SQLException e) {
            logger.error("Error checking table existence: {}", e.getMessage());
            return false;
        }
    }
}
