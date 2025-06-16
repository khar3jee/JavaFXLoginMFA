package dev.dcardenas.javafxloginmfa.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NetInfo {

    private static final Logger logger = LoggerFactory.getLogger(NetInfo.class);

    /**
     * Returns the local IP address of the system.
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out inactive interfaces
                if (!iface.isUp() || iface.isLoopback())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("NetInfo failed to obtain local IP {}", e.getMessage());
        }
        return "Unknown";
    }

    /**
     * Returns the public IP address by querying an external service.
     */
    public static String getPublicIpAddress() {
        try {
            URL url = new URL("https://checkip.amazonaws.com/");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            return br.readLine();
        } catch (Exception e) {
            logger.error("NetInfo failed to obtain public IP {}", e.getMessage());
        }
        return "Unknown";
    }

    /**
     * Returns the MAC address of the primary network interface.
     */
    public static String getMacAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (!iface.isUp() || iface.isLoopback() || iface.isVirtual())
                    continue;

                byte[] mac = iface.getHardwareAddress();
                if (mac == null) continue;

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                }
                return sb.toString();
            }
        } catch (Exception e) {
            logger.error("NetInfo failed to get MAC address {}", e.getMessage());
        }
        return "Unknown";
    }

}