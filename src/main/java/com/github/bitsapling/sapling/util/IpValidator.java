package com.github.bitsapling.sapling.util;

import lombok.SneakyThrows;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpValidator {
    private static final InetAddressValidator ipValidator = InetAddressValidator.getInstance();
    private final static int PORT_MAX_RANGE = 65535;
    private final static int PORT_MIN_RANGE = 10000;

    /**
     * Validate the port
     *
     * @param port the port to validate
     * @return valid or not
     */
    public static boolean isPortValid(int port) {
        // Usually the ports < 10000 may host a service, we need prevent client announce it
        // may cause DDoS attacks
        if (port > PORT_MAX_RANGE) {
            return false;
        }
        return port >= PORT_MIN_RANGE;
    }

    /**
     * Verify a IP address is valid
     *
     * @param address The ip address
     * @return Valid or not
     */
    // Suppress UnknownHostException, ipValidator make sure it must
    // be an ip address so it impossible to trigger the DNS lookup
    @SneakyThrows(UnknownHostException.class)
    public static boolean isPortValid(String address) {
        if (!ipValidator.isValid(address)) {
            return false;
        }
        if (ipValidator.isValidInet4Address(address)) {
            return validateAddress(Inet4Address.getByName(address));
        }
        if (ipValidator.isValidInet6Address(address)) {
            return validateAddress(Inet6Address.getByName(address));
        }
        // neither ipv4 nor ipv6
        return false;
    }

    private static boolean validateAddress(InetAddress address) {
        if (address.isAnyLocalAddress()) {
            return false;
        }
        if (address.isLoopbackAddress()) {
            return false;
        }
        return !address.isSiteLocalAddress();
    }
}
