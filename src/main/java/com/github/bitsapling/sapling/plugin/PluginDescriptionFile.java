package com.github.bitsapling.sapling.plugin;

import com.vdurmont.semver4j.Semver;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PluginDescriptionFile {
    @NotNull List<String> getAuthors();

    @NotNull List<String> getContributors();

    @NotNull String getDescription();

    @NotNull Semver getVersion();

    @NotNull String getMain();

    @NotNull String getName();

    @NotNull String getWebsite();

    @NotNull List<String> getScanPackage();
}
