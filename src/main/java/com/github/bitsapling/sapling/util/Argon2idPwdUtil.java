package com.github.bitsapling.sapling.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class Argon2idPwdUtil {
    private final static Argon2PasswordEncoder ENCODER = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public boolean validate(@NotNull String hash, @NotNull String plain) {
        return ENCODER.matches(plain, hash);
    }

    @NotNull
    public String hash(@NotNull String plain) {
        return ENCODER.encode(plain);
    }
}
