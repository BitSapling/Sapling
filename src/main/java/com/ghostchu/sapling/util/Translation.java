package com.ghostchu.sapling.util;

import com.ghostchu.sapling.language.ITranslation;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * Provides the translation phrases
 */

@Component

public class Translation implements ITranslation {
    private static final String DEFAULT_LOCALE = "en_US";

    @NotNull
    public String parse(@NotNull String key, String... args) {
        return parse(DEFAULT_LOCALE, key, (Object[]) args);
    }

    @Override
    public @NotNull String parse(@NotNull String locale, @NotNull String key) {
        return parse(locale, key, (Object[]) null);
    }

    @NotNull
    public String parse(@NotNull String locale, @NotNull String key, Object... args) {
        return "";
    }
}
