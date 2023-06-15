package com.github.bitsapling.sapling.i18n;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SaplingTranslatable {
    private String key;
    private String[] values;

    public SaplingTranslatable(@NotNull String translateIndexKey, @NotNull String... argumentsToFill) {
        this.key = translateIndexKey;
        this.values = argumentsToFill;
    }

    public SaplingTranslatable(@NotNull String fixedText) {
        this.key = "%s";
        this.values = new String[]{fixedText};
    }

    @NotNull
    public String getKey() {
        return key;
    }

    public void setKey(@NotNull String key) {
        this.key = key;
    }

    @NotNull
    public String[] getValues() {
        return values;
    }

    public void setValues(@NotNull String[] values) {
        this.values = values;
    }

    @NotNull
    public Map<String, Object> toMap() {
        return Map.of("_SAPLING_TRANSLATABLE_", "true", "key", key, "values", values);
    }
}
