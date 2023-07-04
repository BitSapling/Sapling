//package com.github.bitsapling.sapling.i18n;
//
//import com.github.bitsapling.sapling.common.MapSerializable;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.io.Serializable;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public class SaplingText implements MapSerializable, Serializable {
//    private final String key;
//    private final String[] values;
//
//    public SaplingText(@NotNull String translateIndexKey, @NotNull String... argumentsToFill) {
//        //noinspection ConstantValue
//        if (translateIndexKey == null) {
//            throw new IllegalStateException("Cannot build SaplingText with empty translateIndexKey when not a fixed text");
//        }
//        this.key = translateIndexKey;
//        this.values = argumentsToFill;
//    }
//
//    public SaplingText(@NotNull String fixedText) {
//        this.key = null;
//        this.values = new String[]{fixedText};
//    }
//
//    @Nullable
//    public String getKey() {
//        return key;
//    }
//
//
//    @NotNull
//    public String[] getValues() {
//        return values;
//    }
//
//    @NotNull
//    @Override
//    public Map<String, Object> toMap() {
//        Map<String, Object> map = new LinkedHashMap<>();
//        map.put("_SAPLING_COMPONENT_", "SaplingText");
//        map.put("translatable", key != null);
//        if (key != null) {
//            map.put("key", key);
//            map.put("values", values);
//        }
//        return map;
//    }
//}
