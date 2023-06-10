//package com.github.bitsapling.sapling.util;
//
//import at.favre.lib.crypto.bcrypt.BCrypt;
//import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;
//import io.micrometer.common.util.StringUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//
//import java.nio.charset.StandardCharsets;
//
///**
// * BCrypt Hash Utility
// */
//@Slf4j
//public class PasswordHash {
//    private static final int COST = 6;
//
//    public static boolean verify(@NotNull String password, @NotNull String hash) {
//        BCrypt.Result result = BCrypt.verifyer(BCrypt.Version.VERSION_2A, LongPasswordStrategies.hashSha512(BCrypt.Version.VERSION_2A))
//                .verify(password.getBytes(StandardCharsets.UTF_8), hash.getBytes(StandardCharsets.UTF_8));
//        if (!result.validFormat) {
//            log.warn("Failed to validate the hash {}, because the format is invalid", hash);
//        }
//        if (StringUtils.isNotEmpty(result.formatErrorMessage)) {
//            log.warn("An error occurred when verifying the password hash {} for {}", result.formatErrorMessage, hash);
//        }
//        return result.verified;
//    }
//
//    public static String hash(@NotNull String password) {
//        return BCrypt.with(BCrypt.Version.VERSION_2A, LongPasswordStrategies.hashSha512(BCrypt.Version.VERSION_2A))
//                .hashToString(COST, password.toCharArray());
//    }
//}
