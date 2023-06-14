package com.github.bitsapling.sapling.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

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

    public static String fromSocketAddress(SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            var ip = ((InetSocketAddress) socketAddress).getAddress().toString();
            if (ip == null) {
                return null;
            }
            if (ip.startsWith("/")) {
                return ip.substring(1);
            }
            return ip;
        }
        return null;
    }
}
