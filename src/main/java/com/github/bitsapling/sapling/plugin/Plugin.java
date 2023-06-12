package com.github.bitsapling.sapling.plugin;

import com.github.bitsapling.sapling.plugin.java.SaplingPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;

public interface Plugin {
    void onLoad();

    void onEnable();

    void onDisable();

    @NotNull File getDataFolder();

    @NotNull PluginDescriptionFile getDescription();

    @NotNull Logger getLogger();

    @NotNull Class<SaplingPlugin> getMainClass();
}
