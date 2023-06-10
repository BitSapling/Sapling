package com.github.bitsapling.sapling.autoconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class WorkDirectoryConfig {
    @Bean(name = "publicDirectory")
    public File publicDirectory() {
        File file = new File("public");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    
}
