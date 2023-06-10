package com.github.bitsapling.sapling.util;

import com.dampcake.bencode.Bencode;
import com.github.bitsapling.sapling.entity.Peer;
import com.github.bitsapling.sapling.exception.RetryableAnnounceException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class BencodeUtil {
    private static final Bencode BITTORRENT_STANDARD = new Bencode(StandardCharsets.ISO_8859_1);
    private static final Bencode UTF8_STANDARD = new Bencode(StandardCharsets.UTF_8);

    public static String convertToString(byte[] bytes) {
        return new String(bytes, BITTORRENT_STANDARD.getCharset());
    }

    public static Bencode bittorrent() {
        return BITTORRENT_STANDARD;
    }

    public static Bencode utf8() {
        return UTF8_STANDARD;
    }

    public static String compactPeers(Collection<Peer> peers, boolean isV6) throws RetryableAnnounceException {
        ByteBuffer buffer = ByteBuffer.allocate((isV6 ? 18 : 6) * peers.size());
        for (Peer peer : peers) {
            String ip = peer.getIp();
            try {
                for (byte address : InetAddress.getByName(ip).getAddress()) {
                    buffer.put(address);
                }
                int in = peer.getPort();
                buffer.put((byte) ((in >>> 8) & 0xFF));
                buffer.put((byte) (in & 0xFF));
            } catch (UnknownHostException e) {
                throw new RetryableAnnounceException("incorrect ip format encountered when compact peer ip", 0);
            }
        }
        return convertToString(buffer.array());
    }
}
