package dev.dcardenas.javafxloginmfa.util;

/**
 * Helper class to identify system information such as operating system, java,
 * encoding set and instruction set architecture
 */
import java.util.Locale;

public final class SystemInfo {

    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String ARCH = System.getProperty("os.arch");
    private static final String JAVA_VERSION = System.getProperty("java.version");
    private static final String USER = System.getProperty("user.name");
    private static final String ENCODING = System.getProperty("file.encoding");

    private SystemInfo() {}

    public static String getOsName() {
        return OS;
    }

    public static String getArchitecture() {
        return ARCH;
    }

    public static String getJavaVersion() {
        return JAVA_VERSION;
    }

    public static String getCurrentUser() {
        return USER;
    }

    public static String getDefaultEncoding() {
        return ENCODING;
    }

    public static Locale getDefaultLocale() {
        return Locale.getDefault();
    }

    public static boolean isHeadless() {
        return java.awt.GraphicsEnvironment.isHeadless();
    }
}
