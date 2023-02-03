package com.github.bitsapling.sapling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SaplingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaplingApplication.class, args);
    }

}
