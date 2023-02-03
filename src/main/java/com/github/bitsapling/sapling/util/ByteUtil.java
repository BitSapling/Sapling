package com.github.bitsapling.sapling.util;
// https://blog.csdn.net/weixin_38820375/article/details/88556771
public class ByteUtil {
    public static byte[] short2byte(short s) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = 16 - (i + 1) * 8;
            b[i] = (byte) ((s >> offset) & 0xff);
        }
        return b;
    }

    public static short byte2short(byte[] b) {
        short l = 0;
        for (int i = 0; i < 2; i++) {
            l <<= 8;
            l |= (b[i] & 0xff);
        }
        return l;
    }
}
