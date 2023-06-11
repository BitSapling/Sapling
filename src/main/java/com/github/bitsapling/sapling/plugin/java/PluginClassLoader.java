package com.github.bitsapling.sapling.plugin.java;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader {
    private static final String[] BLACKLISTED_CLASS_PREFIXES = {
            "com.github.bitsapling.sapling"
    };

    public PluginClassLoader(String name, URL[] urls, ClassLoader parent) {
        super(name, urls, parent);
    }

}
