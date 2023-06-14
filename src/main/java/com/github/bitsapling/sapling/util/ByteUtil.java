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

    public static long byte2long(byte[] b) {
        return byte2long(b, 0);
    }

    public static long byte2long(byte[] b, int start) {
        long l = 0;
        for (int i = start; i < start + 8; i++) {
            l <<= 8;
            l |= (b[i] & 0xff);
        }
        return l;
    }

    public static int byte2int(byte[] b) {
        return byte2int(b, 0);
    }

    public static int byte2int(byte[] b, int start) {
        int l = 0;
        for (int i = start; i < start + 4; i++) {
            l <<= 8;
            l |= (b[i] & 0xff);
        }
        return l;
    }

    public static byte[] int2byte(int s) {
        byte[] b = new byte[4];
        int2byte(s, b, 0);
        return b;
    }

    public static void int2byte(int s, byte[] b, int start) {
        for (int i = start; i < start + 4; i++) {
            int offset = 16 - (i + 1) * 8;
            b[i] = (byte) ((s >> offset) & 0xff);
        }
    }

    public static byte[] long2byte(long s) {
        byte[] b = new byte[8];
        long2byte(s, b, 0);
        return b;
    }
    public static void long2byte(long s, byte[] b, int start) {
        for (int i = start; i < start + 8; i++) {
            int offset = 16 - (i + 1) * 8;
            b[i] = (byte) ((s >> offset) & 0xff);
        }
    }
}
