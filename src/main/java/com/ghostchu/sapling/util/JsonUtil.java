package com.ghostchu.sapling.util;

import com.nimbusds.jose.shaded.gson.Gson;
import org.jetbrains.annotations.NotNull;

public class JsonUtil {
    private static final Gson GSON = new Gson();

    @NotNull
    public static Gson getGson() {
        return GSON;
    }
}
