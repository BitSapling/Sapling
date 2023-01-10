package com.github.bitsapling.sapling.util;

public class PortValidator {
    private final static int PORT_MAX_RANGE = 65535;
    private final static int PORT_MIN_RANGE = 10000;

    /**
     * Validate the port
     *
     * @param port the port to validate
     * @return valid or not
     */
    public static boolean isValid(int port) {
        // Usually the ports < 10000 may host a service, we need prevent client announce it
        // may cause DDoS attacks
        if (port > PORT_MAX_RANGE) {
            return false;
        }
        return port >= PORT_MIN_RANGE;
    }
}
