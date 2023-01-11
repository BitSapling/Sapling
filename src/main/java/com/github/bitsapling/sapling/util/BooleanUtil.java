package com.github.bitsapling.sapling.util;

import org.jetbrains.annotations.Nullable;

public class BooleanUtil {
    public static boolean parseBoolean(@Nullable String input){
        if(input == null) return false;
        if("1".equals(input)) return true;
        if("true".equalsIgnoreCase(input)) return true;
        if("yes".equalsIgnoreCase(input)) return true;
        if("on".equalsIgnoreCase(input)) return true;
        return "y".equalsIgnoreCase(input);
    }
}
