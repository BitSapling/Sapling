package com.ghostchu.sapling.util;

import lombok.SneakyThrows;

import java.security.MessageDigest;

public class HashUtil {
    @SneakyThrows
    public static String sha1(byte[] bytes) {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
