package com.github.bitsapling.sapling.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class Argon2idPwdUtil {
    private final static int SALT_LENGTH = 16384;
    private final static int HASH_LENGTH = 8;
    private final static int PARALLELISM = 4;
    private final static int MEMORY = 32;
    private final static int ITERATIONS = 64;
    private final static Argon2PasswordEncoder ENCODER = new Argon2PasswordEncoder(SALT_LENGTH, HASH_LENGTH, PARALLELISM, MEMORY, ITERATIONS);

    public boolean validate(@NotNull String hash, @NotNull String plain) {
        return ENCODER.matches(plain, hash);
    }

    @NotNull
    public String hash(@NotNull String plain) {
        return ENCODER.encode(plain);
    }
}
