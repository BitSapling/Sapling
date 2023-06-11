package com.github.bitsapling.sapling;

import com.github.bitsapling.sapling.plugin.PluginManager;
import com.github.bitsapling.sapling.plugin.java.SaplingPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SaplingApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaplingApplication.class);
    private static PluginManager pluginManager;

    public static void main(String[] args) {
        List<Class<?>> initClasses = new ArrayList<>();
        initClasses.add(SaplingApplication.class);
        initClasses.addAll(collectPluginClasses());
        LOGGER.info("Loading SpringBoot with " + initClasses.size() + " initialize classes.");
        SpringApplication.run(initClasses.toArray(new Class[0]), args);
    }

    private static List<Class<SaplingPlugin>> collectPluginClasses() {
        pluginManager = new PluginManager();
        pluginManager.setLoading(true);
        LOGGER.info("Loading plugins, please wait...");
        try {
            pluginManager.loadPlugins();
        } catch (Exception e) {
            LOGGER.error("Failed to load plugins", e);
        }
        pluginManager.setLoading(false);
        return pluginManager.getAllPlugins().stream().map(SaplingPlugin::getMainClass).toList();
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

}
