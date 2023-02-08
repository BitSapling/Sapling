package com.github.bitsapling.sapling.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
    private static Gson GSON = new Gson();
    private static Gson HUMAN_READABLE = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public static Gson getGSON() {
        return GSON;
    }

    public static Gson getHumanReadable() {
        return HUMAN_READABLE;
    }
}
