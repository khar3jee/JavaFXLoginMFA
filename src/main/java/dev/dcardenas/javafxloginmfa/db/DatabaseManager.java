package dev.dcardenas.javafxloginmfa.db;
/**
 * Store failed login attempts and lockout status.
 */

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseManager {
    private static final String DB_PATH = Paths.get(System.getProperty("user.dir"), "db", "javafxlogin.sqlite").toString();
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static boolean databaseInitialized = false; // Prevent multiple inits

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed!", e);
        }
    }
    public static void helperWipeDatabase() {
        //todo helper method to drop tables and delete database files as needed while testing
    }
    public static void ensureDatabaseSetup() {
        if (databaseInitialized) {
            System.out.println("Database already initialized. Skipping setup.");
            return;
        }
        initializeDatabase();
        databaseInitialized = true;
    }

    private static void initializeDatabase() {
        ensureDbDirectoryExists();
        checkDatabaseFile();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            if (!doesTableExist("users")) {
        stmt.execute(
            """
                    CREATE TABLE users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT UNIQUE NOT NULL,
                        password_hash TEXT NOT NULL,
                        firstname TEXT NOT NULL,
                        lastname TEXT NOT NULL,
                        email TEXT UNIQUE NOT NULL,
                        failed_attempts INTEGER DEFAULT 0,
                        locked_until DATETIME NULL,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                    );
                """);
                System.out.println("Users table created.");
            } else {
                System.out.println("Users table already exists.");
            }

            if (!doesTableExist("audit_log")) {
                stmt.execute("""
                    CREATE TABLE audit_log (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        username TEXT,
                        action TEXT NOT NULL,
                        event_type TEXT NOT NULL,
                        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                        source_ip TEXT,
                        outcome TEXT NOT NULL
                    );
                """);
                System.out.println("Audit log table created.");
            } else {
                System.out.println("Audit log table already exists.");
            }

            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing the database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private static void ensureDbDirectoryExists() {
        File dbDir = new File(System.getProperty("user.dir") + "/db/");
        if (!dbDir.exists()) {
            boolean created = dbDir.mkdirs();
            if (created) {
                System.out.println("Created missing directory: " + dbDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create database directory!");
            }
        }
    }

    private static void checkDatabaseFile() {
        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
            System.err.println("Database file not found: " + dbFile.getAbsolutePath());
        } else {
            System.out.println("Database found at: " + dbFile.getAbsolutePath());

            if (!doesTableExist("users")) {
                System.err.println("WARNING: Database file exists, but 'users' table is missing!");
            }
        }
    }

    private static boolean doesTableExist(String tableName) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';")) {
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking table existence: " + e.getMessage());
            return false;
        }
    }
}
