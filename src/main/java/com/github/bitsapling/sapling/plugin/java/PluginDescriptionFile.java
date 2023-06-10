package com.github.bitsapling.sapling.plugin.java;

import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Pattern;

public class PluginDescriptionFile {
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[A-Za-z0-9 _.-]+$");
    private final String name;
    private final String main;
    private final String versionPlain;
    private final String description;
    private final List<String> authors;
    private final List<String> contributors;
    private final String website;
    private final YamlConfigurationLoader configurationLoader;
    private final CommentedConfigurationNode configNode;
    private Semver version = null;

    public PluginDescriptionFile(String yaml) throws ConfigurateException, PluginDescriptionFileException {
        if (yaml == null) {
            throw new PluginDescriptionFileException("PluginDescriptionFile input cannot be null");
        }
        configurationLoader = YamlConfigurationLoader.builder()
                .source(() -> new BufferedReader(new StringReader(yaml)))
                .nodeStyle(NodeStyle.BLOCK)
                .build();
        configNode = configurationLoader.load();
        this.name = configNode.node("name").getString();
        this.main = configNode.node("main").getString();
        this.versionPlain = configNode.node("version").getString();
        this.description = configNode.node("description").getString();
        this.authors = configNode.node("authors").getList(String.class);
        this.contributors = configNode.node("contributors").getList(String.class);
        this.website = configNode.node("website").getString();
        validateAndGenerate();
    }

    private void validateAndGenerate() throws PluginDescriptionFileException {
        if (!VALID_NAME_PATTERN.matcher(this.name).matches()) {
            throw new PluginDescriptionFileException("Invalid plugin name: " + this.name + ", it must match " + VALID_NAME_PATTERN.pattern());
        }
        if (this.main == null) {
            throw new PluginDescriptionFileException("Invalid plugin main: " + this.main + ", it must not be null");
        }
        if (this.versionPlain == null) {
            throw new PluginDescriptionFileException("Invalid plugin version: " + this.versionPlain + ", it must not be null");
        }
        try {
            this.version = new Semver(this.versionPlain);
        } catch (SemverException e) {
            throw new PluginDescriptionFileException("Invalid plugin version: " + this.versionPlain + ", it must be a valid semantic version");
        }
        if (this.description == null) {
            throw new PluginDescriptionFileException("Invalid plugin description: " + this.description + ", it must not be null");
        }
        if (this.authors == null) {
            throw new PluginDescriptionFileException("Invalid plugin authors: " + this.authors + ", it must not be null");
        }
        if (this.contributors == null) {
            throw new PluginDescriptionFileException("Invalid plugin contributors: " + this.contributors + ", it must not be null");
        }
        if (this.website == null) {
            throw new PluginDescriptionFileException("Invalid plugin website: " + this.website + ", it must not be null");
        }
    }

    @NotNull
    public List<String> getAuthors() {
        return authors;
    }

    @NotNull
    public List<String> getContributors() {
        return contributors;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public Semver getVersion() {
        return version;
    }

    @NotNull
    public String getMain() {
        return main;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getWebsite() {
        return website;
    }

    public static class PluginDescriptionFileException extends Exception {
        public PluginDescriptionFileException(String message) {
            super(message);
        }
    }
}
