package com.github.bitsapling.sapling.plugin.java;

import com.github.bitsapling.sapling.plugin.Plugin;
import com.github.bitsapling.sapling.plugin.PluginDescriptionFile;
import com.github.bitsapling.sapling.plugin.PluginManager;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SaplingPluginManager implements PluginManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SaplingPluginManager.class);
    private final File pluginsDirectory = new File("plugins");
    private final PluginClassLoader pluginClassLoader = new PluginClassLoader(
            "SaplingPluginClassLoader",
            new URL[0],
            this.getClass().getClassLoader());
    private final List<SaplingPlugin> plugins = new ArrayList<>();
    private final Map<String, SaplingPlugin> lookupTable = new HashMap<>();
    private boolean isLoading;

    public void loadPlugins() {
        if (!isLoading) {
            throw new IllegalStateException("Cannot load plugins while not loading");
        }
        if (!pluginsDirectory.exists()) {
            if (!pluginsDirectory.mkdirs()) {
                throw new IllegalStateException("Could not create plugins directory at " + pluginsDirectory.getAbsolutePath());
            }
        }
        LOGGER.info("Loading plugins...");
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
                LOGGER.info("Found jar: " + pluginFile.getName());
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

    public void loadPlugin(@NotNull File pluginFile) throws IOException, PluginDescriptionFileException, ClassNotFoundException, InvalidPluginException {
        if (!isLoading) {
            throw new IllegalStateException("Cannot load plugins while not loading");
        }
        if (!pluginFile.exists()) {
            throw new IOException("Plugin file does not exist at " + pluginFile.getAbsolutePath());
        }
        try (JarFile jar = new JarFile(pluginFile)) {
            JarEntry jarEntry = jar.getJarEntry("plugin.yml");
            if (jarEntry == null) {
                throw new IOException("The file " + pluginFile.getName() + " not a valid plugin file, missing plugin.yml");
            }
            PluginDescriptionFile descriptionFile = new SaplingPluginDescriptionFile(new String(jar.getInputStream(jarEntry).readAllBytes(), StandardCharsets.UTF_8));
            LOGGER.info("Loading plugin {}" + " v{}...", descriptionFile.getName(), descriptionFile.getVersion());
            SaplingPlugin saplingPlugin;
            try {
                saplingPlugin = loadPluginToSaplingPlugin(pluginFile, descriptionFile);
            } catch (InvalidPluginException | NoSuchMethodException | InvocationTargetException |
                     InstantiationException | IllegalAccessException e) {
                throw new InvalidPluginException("Failed to load plugin " + descriptionFile.getName(), e);
            }
            if (lookupTable.containsKey(saplingPlugin.getDescription().getName())) {
                PluginDescriptionFile conflictWith = lookupTable.get(saplingPlugin.getDescription().getName()).getDescription();
                PluginDescriptionFile thisPlugin = saplingPlugin.getDescription();
                LOGGER.warn("Plugin {} version {} (file: {}) name identifier are conflicting with Plugin {} version {} (file: {}), the Plugin {} version {} (file: {}) won't be loaded!"
                        , thisPlugin.getName(), thisPlugin.getVersion(), saplingPlugin.getPluginFile().getAbsolutePath(),
                        conflictWith.getName(), conflictWith.getVersion(), lookupTable.get(saplingPlugin.getDescription().getName()).getPluginFile().getAbsolutePath(),
                        thisPlugin.getName(), thisPlugin.getVersion(), saplingPlugin.getPluginFile().getAbsolutePath());
                throw new InvalidPluginException("Plugin identifier conflict with another already loaded plugin.");
            }
            plugins.add(saplingPlugin);
            lookupTable.put(descriptionFile.getName(), saplingPlugin);
            saplingPlugin.onLoad();
        }
    }

    @NotNull
    private SaplingPlugin loadPluginToSaplingPlugin(@NotNull File pluginFile, @NotNull PluginDescriptionFile descriptionFile) throws MalformedURLException, InvalidPluginException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try {
            testPluginMainValidate(pluginFile, descriptionFile);
        } catch (Exception e) {
            throw new InvalidPluginException("Failed to validate plugin " + descriptionFile.getName(), e);
        }
        pluginClassLoader.addURL(pluginFile.toURI().toURL());
        Class<?> mainClass = pluginClassLoader.loadClass(descriptionFile.getMain());
        Constructor<?> constructor = mainClass.getDeclaredConstructor(ClassLoader.class, Class.class, Logger.class, File.class, SaplingPluginDescriptionFile.class, File.class);
        return (SaplingPlugin) constructor.newInstance(pluginClassLoader, mainClass, LoggerFactory.getLogger(descriptionFile.getName()), pluginsDirectory, descriptionFile, pluginFile);
    }

    private void testPluginMainValidate(@NotNull File pluginFile, @NotNull PluginDescriptionFile descriptionFile) throws IOException, ClassNotFoundException, InvalidPluginException, NoSuchMethodException {
        try (PluginClassLoader validateClassLoader = new PluginClassLoader("validating-" + descriptionFile.getName(),
                new URL[]{pluginFile.toURI().toURL()},
                this.getClass().getClassLoader())) {
            Class<?> main = validateClassLoader.loadClass(descriptionFile.getMain());
            if (!SaplingPlugin.class.isAssignableFrom(main)) {
                throw new InvalidPluginException("Main class " + descriptionFile.getMain() + " does not extend SaplingPlugin");
            }
            Constructor<?> constructorTest = main.getDeclaredConstructor(ClassLoader.class, Class.class, Logger.class, File.class, SaplingPluginDescriptionFile.class, File.class);
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    @NotNull
    public List<Plugin> getAllPlugins() {
        return ImmutableList.copyOf(this.plugins);
    }

    @Override
    public @Nullable Plugin getPlugin(@NotNull String identifier) {
        return lookupTable.get(identifier);
    }

    public void enablePlugin() {
        if (!isLoading) {
            throw new IllegalStateException("Cannot load plugins while not loading");
        }
        LOGGER.info("Enabling plugins...");
        for (SaplingPlugin plugin : plugins) {
            try {
                plugin.getLogger().info("Enabling plugin " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion() + "...");
                plugin.onEnable();
            } catch (Throwable e) {
                LOGGER.warn("Failed to enable plugin " + plugin.getDescription().getName(), e);
            }
        }
    }
}
