package com.github.bitsapling.sapling.plugin.java;

import org.slf4j.Logger;

import java.io.File;

public abstract class SaplingPlugin {
    private boolean isEnabled = false;
    private File pluginFile = null;
    private PluginDescriptionFile description = null;
    private File dataFolder = null;
    private File configFile = null;
    private Logger pluginLogger = null;

    public abstract void onLoad();

    public abstract void onEnable();

    public abstract void onDisable();

    public File getDataFolder() {
        return dataFolder;
    }

    public PluginDescriptionFile getDescription() {
        return description;
    }

    public Logger getLogger() {
        return pluginLogger;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
