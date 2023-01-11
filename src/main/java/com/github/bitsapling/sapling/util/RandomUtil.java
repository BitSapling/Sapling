package com.github.bitsapling.sapling.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtil {
    private static final Random rand = new Random();
    @NotNull
    public static <T> List<T> getRandomElements(List<T> list, int want) {
        List<T> listCopy = new ArrayList<>(list);
        List<T> result = new ArrayList<>(list);
        if(list.size() <= want) return listCopy;
        for (int i = 0; i < want; i++) {
            T obj = listCopy.get(rand.nextInt(listCopy.size()));
            result.add(obj);
            listCopy.remove(obj);
        }
        return result;
    }
}
