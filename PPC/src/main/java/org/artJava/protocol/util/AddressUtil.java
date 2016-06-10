package org.artJava.protocol.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created on 5/29/15.
 */
public class AddressUtil {

    private static final AddressUtil INSTANCE = new AddressUtil();

    private String macAddress;
    private String ipAddress;

    public static AddressUtil getInstance() {
        return INSTANCE;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getIPAddress() {
        return ipAddress;
    }

    private AddressUtil() {
        macAddress = null;
        ipAddress = null;
        try {
            initIPAddress();
        } catch (SocketException e) {
            ipAddress = null;
        }
        try {
            initMacAddress();
        } catch (SocketException e) {
            macAddress = null;
        }
    }

    public void initMacAddress() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            byte[] macAddress = interfaces.nextElement().getHardwareAddress();
            if (macAddress != null) {
                StringBuilder sb = new StringBuilder();
                for (byte b : macAddress) {
                    sb.append(String.format("%02X", b));
                }
                this.macAddress = sb.toString();
            }
        }
    }

    public void initIPAddress() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface ele = interfaces.nextElement();
            byte[] macAddress = ele.getHardwareAddress();
            if (macAddress != null) {
                Enumeration<InetAddress> inetAddresses = ele.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    String hostAddress = inetAddresses.nextElement().getHostAddress();
                    if (hostAddress.matches("(\\d{1,3}\\.){3}\\d{1,3}")) {
                        ipAddress = hostAddress;
                    }
                }
            }
        }
    }
}
