package com.ghostchu.sapling.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;
import io.micrometer.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class PasswordHash {
    private static final Logger LOG = LoggerFactory.getLogger(PasswordHash.class);
    private static final int COST = 6;

    public static boolean verify(@NotNull String password, @NotNull String hash) {
        BCrypt.Result result = BCrypt.verifyer(null, LongPasswordStrategies.hashSha512(BCrypt.Version.VERSION_2A))
                .verify(password.getBytes(StandardCharsets.UTF_8), hash.getBytes(StandardCharsets.UTF_8));
        if (!result.validFormat) {
            LOG.warn("Failed to validate the hash {}, because the format is invalid", hash);
        }
        if (StringUtils.isNotEmpty(result.formatErrorMessage)) {
            LOG.warn("An error occurred when verifying the password hash {} for {}", result.formatErrorMessage, hash);
        }
        return result.verified;
    }

    public static String hash(@NotNull String password) {
        return BCrypt.with(null, LongPasswordStrategies.hashSha512(BCrypt.Version.VERSION_2A))
                .hashToString(COST, password.toCharArray());
    }
}
