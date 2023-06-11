package com.github.bitsapling.sapling.plugin;

import com.github.bitsapling.sapling.plugin.java.*;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
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
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);
    private final File pluginsDirectory = new File("plugins");
    private final PluginClassLoader pluginClassLoader = new PluginClassLoader(
            "SaplingPluginClassLoader",
            new URL[0],
            this.getClass().getClassLoader());
    private final List<SaplingPlugin> plugins = new ArrayList<>();
    private boolean isLoading;

    public void loadPlugins() throws IOException {
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
            PluginDescriptionFile descriptionFile = new PluginDescriptionFile(new String(jar.getInputStream(jarEntry).readAllBytes(), StandardCharsets.UTF_8));
            LOGGER.info("Loading plugin " + descriptionFile.getName() + " v" + descriptionFile.getVersion() + "...");
            SaplingPlugin saplingPlugin;
            try {
                saplingPlugin = loadPluginToSaplingPlugin(pluginFile, descriptionFile);
            } catch (InvalidPluginException | NoSuchMethodException | InvocationTargetException |
                     InstantiationException | IllegalAccessException e) {
                throw new InvalidPluginException("Failed to load plugin " + descriptionFile.getName(), e);
            }
            plugins.add(saplingPlugin);
            saplingPlugin.onLoad();
        }
    }

    private SaplingPlugin loadPluginToSaplingPlugin(File pluginFile, PluginDescriptionFile descriptionFile) throws MalformedURLException, InvalidPluginException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try {
            testPluginMainValidate(pluginFile, descriptionFile);
        } catch (Exception e) {
            throw new InvalidPluginException("Failed to validate plugin " + descriptionFile.getName(), e);
        }
        pluginClassLoader.addURL(pluginFile.toURI().toURL());
        Class<?> mainClass = pluginClassLoader.loadClass(descriptionFile.getMain());
        Constructor<?> constructor = mainClass.getDeclaredConstructor(ClassLoader.class, Class.class, Logger.class, File.class, PluginDescriptionFile.class, File.class);
        return (SaplingPlugin) constructor.newInstance(pluginClassLoader, mainClass, LoggerFactory.getLogger(descriptionFile.getName()), pluginsDirectory, descriptionFile, pluginFile);
    }

    private void testPluginMainValidate(File pluginFile, PluginDescriptionFile descriptionFile) throws IOException, ClassNotFoundException, InvalidPluginException, NoSuchMethodException {
        try (PluginClassLoader validateClassLoader = new PluginClassLoader("validating-" + descriptionFile.getName(),
                new URL[]{pluginFile.toURI().toURL()},
                this.getClass().getClassLoader())) {
            Class<?> main = validateClassLoader.loadClass(descriptionFile.getMain());
            if (!SaplingPlugin.class.isAssignableFrom(main)) {
                throw new InvalidPluginException("Main class " + descriptionFile.getMain() + " does not extend SaplingPlugin");
            }
            Constructor<?> constructorTest = main.getDeclaredConstructor(ClassLoader.class, Class.class, Logger.class, File.class, PluginDescriptionFile.class, File.class);
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public List<SaplingPlugin> getAllPlugins() {
        return ImmutableList.copyOf(this.plugins);
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
