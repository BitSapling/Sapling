package com.github.bitsapling.sapling.util;

import com.dampcake.bencode.Bencode;

import java.nio.charset.StandardCharsets;

public class BencodeUtil {
    private static final Bencode BITTORRENT_STANDARD = new Bencode(StandardCharsets.ISO_8859_1);
    private static final Bencode UTF8_STANDARD = new Bencode(StandardCharsets.UTF_8);
    public static String convertToString(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append((char) b);
        }
        return stringBuilder.toString();
    }

    public static Bencode bittorrent(){
        return BITTORRENT_STANDARD;
    }

    public static Bencode utf8(){
        return UTF8_STANDARD;
    }
}
