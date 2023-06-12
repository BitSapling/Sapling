package com.github.bitsapling.sapling.plugin.java;

import com.github.bitsapling.sapling.plugin.Plugin;
import com.github.bitsapling.sapling.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;

public abstract class SaplingPlugin implements Plugin {
    private final File pluginFile;
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final Logger pluginLogger;
    private final ClassLoader classloader;
    private final Class<SaplingPlugin> mainClass;
    private boolean isEnabled = false;

    protected SaplingPlugin(@NotNull ClassLoader classLoader, @NotNull Class<SaplingPlugin> mainClass, @NotNull Logger logger, @NotNull File pluginFile, @NotNull PluginDescriptionFile descriptionFile, @NotNull File dataFolder) {
        // check if classLoader is com.github.bitsapling.sapling.plugin.java.PluginClassLoader
        if (!(classLoader instanceof PluginClassLoader)) {
            throw new IllegalArgumentException("ClassLoader must be an instance of PluginClassLoader");
        }
        this.classloader = classLoader;
        this.mainClass = mainClass;
        this.pluginLogger = logger;
        this.pluginFile = pluginFile;
        this.description = descriptionFile;
        this.dataFolder = dataFolder;
    }

    @Override
    @NotNull
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    @NotNull
    public PluginDescriptionFile getDescription() {
        return description;
    }

    @Override
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
    public File getPluginFile() {
        return pluginFile;
    }

    @Override
    @NotNull
    public Class<SaplingPlugin> getMainClass() {
        return mainClass;
    }

    @NotNull
    public ClassLoader getClassloader() {
        return classloader;
    }
}
