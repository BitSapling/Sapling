package com.github.bitsapling.sapling.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class IPUtil {
    @NotNull
    public static String getRequestIp(@NotNull HttpServletRequest request) {
        String realIp = request.getHeader("X-REAL-IP");
        if (realIp == null)
            realIp = request.getHeader("X-FORWARDED-FOR");
        if (realIp == null)
            realIp = request.getRemoteAddr();
        return realIp;
    }

    public static boolean isIPv4(String input) {
        try {
            InetAddress inetAddress = InetAddress.getByName(input);
            return (inetAddress instanceof Inet4Address) && inetAddress.getHostAddress().equals(input);
        } catch (UnknownHostException ex) {
            return false;
        }
    }

    public static boolean isIPv6(String input) {
        try {
            InetAddress inetAddress = InetAddress.getByName(input);
            return (inetAddress instanceof Inet6Address);
        } catch (UnknownHostException ex) {
            return false;
        }
    }
}
