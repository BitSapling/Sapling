package com.github.bitsapling.sapling.plugin.java;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

public class PluginClassLoader extends URLClassLoader {
    private static final Set<String> BLACKLISTED_CLASS_PREFIXES = Set.of(new String[]{
            "com.github.bitsapling.sapling"
    });

    public PluginClassLoader(String name, URL[] urls, ClassLoader parent) {
        super(name, urls, parent);
    }

    @Override
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        if (className.startsWith("java.")) {
            return findSystemClass(className);
        }
        if (BLACKLISTED_CLASS_PREFIXES.stream().anyMatch(className::startsWith)) {
            return getParent().loadClass(className);
        }
        return super.loadClass(className);
    }

}
