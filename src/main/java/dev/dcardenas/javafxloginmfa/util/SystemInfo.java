package dev.dcardenas.javafxloginmfa.util;

/**
 * Helper class to identify system information such as operating system, java,
 * encoding set and instruction set architecture
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.util.Locale;

public final class SystemInfo {

    private static final Logger logger = LoggerFactory.getLogger(SystemInfo.class);

    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String ARCH = System.getProperty("os.arch");
    private static final String JAVA_VERSION = System.getProperty("java.version");
    private static final String USER = System.getProperty("user.name");
    private static final String ENCODING = System.getProperty("file.encoding");
    private static final long MEMORY = Runtime.getRuntime().totalMemory();
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();;
    private static final String SCREEN_RESOLUTION = InitScreenResolution();

    private SystemInfo() {
        // prevent instantiation
    }

    public static String InitScreenResolution() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            DisplayMode dm = gd.getDisplayMode();
            int width = dm.getWidth();
            int height = dm.getHeight();
            int refreshRate = dm.getRefreshRate();
            return String.format("%dx%d @%dHz", width, height, refreshRate);
        } catch (Exception e) {
            logger.error("SystemInfo error, failed to obtain screen resolution{}", e.getMessage());
            return "Unknown";
        }
    }

    public static String getOSName() {
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

    public static long getTotalMemory() {
        return MEMORY;
    }

    public static int getCpuCoreCount() {
        return CPU_COUNT;
    }

    public static String getScreenResolution() {
        return SCREEN_RESOLUTION;
    }

    public static boolean isHeadless() {
        return GraphicsEnvironment.isHeadless();
    }
}
