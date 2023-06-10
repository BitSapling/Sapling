package com.github.bitsapling.sapling;

import com.github.bitsapling.sapling.plugin.PluginManager;
import com.github.bitsapling.sapling.plugin.java.DummyScanObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SaplingApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaplingApplication.class);
    @SuppressWarnings("FieldCanBeLocal")
    private PluginManager pluginManager;

    public static void main(String[] args) throws Throwable {
        DummyScanObject scanObject = new DummyScanObject();
        SpringApplication.run(SaplingApplication.class, args);
    }

    private void preInit() {
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
