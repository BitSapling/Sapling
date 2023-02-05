package com.github.bitsapling.sapling.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class WorkDirectoryConfig {
    @Bean(name = "publicDirectory")
    public File publicDirectory(){
        File file = new File("public");
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }
    @Bean(name = "torrentsDirectory")
    public File torrentsDirectory(){
        File file = new File(publicDirectory(), "torrents");
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }
}
