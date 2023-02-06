package com.github.bitsapling.sapling.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class URLEncodeUtil {
    private static final int RADIX = 16;
    private static final BitSet URLENCODER = new BitSet(256);

    // From Apache HttpClient URLEncodedUtils
    public static String urlEncode(
            final String content,
            final boolean blankAsPlus) {
        if (content == null) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        final ByteBuffer bb = StandardCharsets.UTF_8.encode(content);
        while (bb.hasRemaining()) {
            final int b = bb.get() & 0xff;
            if (URLENCODER.get(b)) {
                buf.append((char) b);
            } else if (blankAsPlus && b == ' ') {
                buf.append('+');
            } else {
                buf.append("%");
                final char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, RADIX));
                final char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, RADIX));
                buf.append(hex1);
                buf.append(hex2);
            }
        }
        return buf.toString();
    }

}
