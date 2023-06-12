package com.github.bitsapling.sapling.plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PluginManager {
    @NotNull List<Plugin> getAllPlugins();

    @Nullable Plugin getPlugin(@NotNull String identifier);
}
