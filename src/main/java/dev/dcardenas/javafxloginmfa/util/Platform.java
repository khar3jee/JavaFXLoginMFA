package dev.dcardenas.javafxloginmfa.util;

/**
 * Helper to identify operating system
 */
public class Platform {
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isUnix() {
        return OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    }

    private Platform() {
        // prevent instantiation
    }
}