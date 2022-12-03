package com.ghostchu.sapling.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class NumberUtil {
    private static final NumberFormat SHARE_RATIO_FORMATTER = NumberFormat.getNumberInstance();

    static {
        SHARE_RATIO_FORMATTER.setMaximumIntegerDigits(1);
    }

    @NotNull
    public static String formatShareRatio(BigDecimal d) {
        return SHARE_RATIO_FORMATTER.format(d);
    }
}
