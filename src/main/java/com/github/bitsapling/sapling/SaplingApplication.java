package com.github.bitsapling.sapling;

import com.github.bitsapling.sapling.plugin.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SaplingApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaplingApplication.class);
    private static PluginManager pluginManager;

    public static void main(String[] args) {
        List<Class<?>> initClasses = new ArrayList<>();
        initClasses.add(SaplingApplication.class);
        initClasses.addAll(preInit());
        SpringApplication.run(initClasses.toArray(new Class[0]), args);
    }

    private static List<Class<?>> preInit() {
        pluginManager = new PluginManager();
        pluginManager.setLoading(true);
        LOGGER.info("Loading plugins, please wait...");
        try {
            pluginManager.loadPlugins();
        } catch (IOException e) {
            LOGGER.error("Failed to load plugins", e);
        }
        pluginManager.setLoading(false);
    }

}
