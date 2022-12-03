package com.ghostchu.sapling.language;

import org.jetbrains.annotations.NotNull;

public interface ITranslation {
    @NotNull
    String parse(@NotNull String locale, @NotNull String key);

    @NotNull
    String parse(@NotNull String locale, @NotNull String key, Object... args);
}
