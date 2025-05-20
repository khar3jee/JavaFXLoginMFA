package dev.dcardenas.javafxloginmfa.security;

import java.util.regex.Pattern;

public class InputValidator {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");

    /**
     * Validates a username.
     * @param username The username to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Validates a password.
     * @param password The password to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Sanitizes input by removing potentially dangerous characters.
     * @param input The input to sanitize.
     * @return A sanitized string.
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        //return input.replaceAll("[<>\\"'%;()&]", "");
        return input.replaceAll("[<>\\\\\"'%;()&]", "");
    }
}
