package com.github.bitsapling.sapling.autoconfig;

import com.github.bitsapling.sapling.SaplingApplication;
import com.github.bitsapling.sapling.plugin.PluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluginManagerConfig {
    @Bean
    public PluginManager pluginManager() {
        return SaplingApplication.getPluginManager();
    }
}
