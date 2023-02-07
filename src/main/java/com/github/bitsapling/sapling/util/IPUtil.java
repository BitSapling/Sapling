package com.github.bitsapling.sapling.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
@Slf4j
public class IPUtil {
    @NotNull
    public static String getRequestIp(@NotNull HttpServletRequest request) {
        String realIp = request.getHeader("CF-CONNECTING-IP");
        if (realIp == null)
            realIp = request.getHeader("X-REAL-IP");
        if (realIp == null)
            realIp = request.getHeader("X-FORWARDED-FOR");
        if (realIp == null)
            realIp = request.getRemoteAddr();
        return realIp;
    }
}
