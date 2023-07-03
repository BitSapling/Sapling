package com.github.bitsapling.sapling.util;

import java.util.regex.Pattern;

public class MagicPattern {
    private static final Pattern infoHashPattern = Pattern.compile("info_hash=(.*?)($|&)");
    private static final Pattern peerIdPattern = Pattern.compile("peer_id=(.*?)&");

    public static Pattern getInfoHashPattern() {
        return infoHashPattern;
    }

    public static Pattern getPeerIdPattern() {
        return peerIdPattern;
    }
}
