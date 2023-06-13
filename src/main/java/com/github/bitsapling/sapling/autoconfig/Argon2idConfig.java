package com.github.bitsapling.sapling.autoconfig;

import com.github.bitsapling.sapling.util.PasswordUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Argon2idConfig {
    @Bean
    public PasswordUtil passwordUtil() {
        return new PasswordUtil();
    }
}
