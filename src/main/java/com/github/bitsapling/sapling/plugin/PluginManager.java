package com.github.bitsapling.sapling.plugin;

import com.github.bitsapling.sapling.plugin.java.PluginDescriptionFile;
import com.github.bitsapling.sapling.plugin.java.PluginDescriptionFileException;
import com.github.bitsapling.sapling.plugin.java.SaplingPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);
    private final File pluginsDirectory = new File("plugins");
    private boolean isLoading;
    private List<SaplingPlugin> plugins = new ArrayList<>();

    public void loadPlugins() throws IOException {
        if (!pluginsDirectory.exists()) {
            if (!pluginsDirectory.mkdirs()) {
                throw new IllegalStateException("Could not create plugins directory at " + pluginsDirectory.getAbsolutePath());
            }
        }
        List<File> pendingForLoading = new ArrayList<>();
        File[] pluginFiles = pluginsDirectory.listFiles();
        if (pluginFiles != null) {
            for (File pluginFile : pluginFiles) {
                if (pluginFile.isDirectory()) { // folder no no no
                    continue;
                }
                if (!pluginFile.getName().endsWith(".jar")) { // myplugin.txt
                    continue;
                }
                if (pluginFile.getName().startsWith(".")) { // .myplugin.jar
                    continue;
                }
                pendingForLoading.add(pluginFile);
            }
        }
        for (File pluginFile : pendingForLoading) {
            try {
                loadPlugin(pluginFile);
            } catch (Throwable th) {
                LOGGER.warn("Failed to load plugin " + pluginFile.getName(), th);
            }
        }
    }

    private void loadPlugin(@NotNull File pluginFile) throws IOException, PluginDescriptionFileException {
        if (!pluginFile.exists()) {
            throw new IOException("Plugin file does not exist at " + pluginFile.getAbsolutePath());
        }
        try (JarFile jar = new JarFile(pluginFile)) {
            JarEntry jarEntry = jar.getJarEntry("plugin.yml");
            if (jarEntry == null) {
                throw new IOException("Not a valid plugin file, missing plugin.yml");
            }
            PluginDescriptionFile descriptionFile = new PluginDescriptionFile(new String(jar.getInputStream(jarEntry).readAllBytes(), StandardCharsets.UTF_8));
            
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }


}
