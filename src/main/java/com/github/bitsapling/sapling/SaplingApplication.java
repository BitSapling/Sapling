package com.github.bitsapling.sapling;

import cn.dev33.satoken.SaManager;
import com.github.bitsapling.sapling.util.PasswordHash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.UUID;

@SpringBootApplication
@EnableCaching
@Slf4j
public class SaplingApplication {

    public static void main(String[] args) {
        System.out.println("Hash: " + PasswordHash.hash("testtest"));
        System.out.println(new UUID(1, 0).toString().replace("_", ""));
        System.out.println(new UUID(2, 0).toString().replace("_", ""));
        SpringApplication.run(SaplingApplication.class, args);
        SaManager.getConfig();
    }

}
