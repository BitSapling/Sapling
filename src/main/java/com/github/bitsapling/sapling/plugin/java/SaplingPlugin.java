package com.github.bitsapling.sapling.plugin.java;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;

public abstract class SaplingPlugin {
    private final File pluginFile;
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final Logger pluginLogger;
    private final ClassLoader classloader;
    private boolean isEnabled = false;

    protected SaplingPlugin(ClassLoader classLoader, @NotNull Logger logger, @NotNull File pluginFile, @NotNull PluginDescriptionFile descriptionFile, @NotNull File dataFolder) {
        this.classloader = classLoader;
        this.pluginLogger = logger;
        this.pluginFile = pluginFile;
        this.description = descriptionFile;
        this.dataFolder = dataFolder;
    }

    public abstract void onLoad();

    public abstract void onEnable();

    public abstract void onDisable();

    @NotNull
    public File getDataFolder() {
        return dataFolder;
    }

    @NotNull
    public PluginDescriptionFile getDescription() {
        return description;
    }

    @NotNull
    public Logger getLogger() {
        return pluginLogger;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    protected void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @NotNull
    protected File getPluginFile() {
        return pluginFile;
    }

    public ClassLoader getPluginClassloader() {
        return classloader;
    }
}
