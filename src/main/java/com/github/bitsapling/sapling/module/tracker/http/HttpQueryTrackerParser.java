package com.github.bitsapling.sapling.module.tracker.http;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.bitsapling.sapling.util.IPUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class HttpQueryTrackerParser {
    private static final URLCodec codec = new URLCodec();
    private static final String[] peerIdAlias = new String[]{"peer_id", "peerid", "peer id"};
    private static final String[] numWantAlias = new String[]{"numwant", "num want", "num_want"};
    private static final String[] noPeerIdAlias = new String[]{"nopeerid", "no_peerid", "no_peer_id", "no peer id"};
    private static final String[] supportCryptoAlias = new String[]{"supportcrypto", "support_crypto", "support crypto"};
    private static final String[] generalIpAddressAlias = new String[]{"ip", "ipaddress", "ip_address", "ip address", "address"};
    private static final String[] redundantAlias = new String[]{"redundant", "redundant_peers", "redundant peers"};
    private final Map<String, String> q;
    private final HttpServletRequest r;

    private final String userAgent;

    private final String[] ipv4;

    private final String[] ipv6;

    private final String[] clientReportIps;

    private final int port;

    private final String passkey;
    private final long left;
    private final String event;
    private final boolean noPeerId;
    private final boolean supportCrypto;
    private final boolean compact;
    private final String infoHash;
    private final long downloaded;
    private final long uploaded;
    private final int redundant;
    private String peerId;
    private int numWant;

    public HttpQueryTrackerParser(HttpServletRequest request, Map<String, String> q) throws DecoderException, RuntimeException {
        this.q = q;
        this.r = request;
        this.passkey = q.get("passkey");
        this.peerId = q.get("peer_id");
        this.userAgent = r.getHeader("User-Agent");
        this.ipv4 = r.getParameterValues("ipv4");
        this.ipv6 = r.getParameterValues("ipv6");
        this.clientReportIps = readMultipleAlias(generalIpAddressAlias, new String[0]);
        this.port = Integer.parseInt(q.get("port"));
        this.peerId = codec.decode(readMultipleAliasOne(peerIdAlias, null));
        this.compact = Boolean.parseBoolean(q.get("compact"));
        this.event = q.get("event");
        this.numWant = Integer.parseInt(readMultipleAliasOne(numWantAlias, "200"));
        this.noPeerId = Boolean.parseBoolean(readMultipleAliasOne(noPeerIdAlias, "false"));
        this.supportCrypto = Boolean.parseBoolean(readMultipleAliasOne(supportCryptoAlias, "false"));
        this.infoHash = codec.decode(q.get("info_hash"));
        this.downloaded = Long.parseLong(q.get("downloaded"));
        this.uploaded = Long.parseLong(q.get("uploaded"));
        this.left = Long.parseLong(q.get("left"));
        this.redundant = Integer.parseInt(readMultipleAliasOne(redundantAlias, "0"));
        validate();
    }

    private void validate() {
        if (StringUtils.isEmpty(passkey)) throw new IllegalArgumentException("passkey cannot be empty");
        if (StringUtils.isEmpty(peerId)) throw new IllegalArgumentException("peer_id cannot be empty");
        if (StringUtils.isEmpty(userAgent)) throw new IllegalArgumentException("user-agent header cannot be empty");
        if (StringUtils.isEmpty(infoHash)) throw new IllegalArgumentException("info_hash cannot be empty");
        if (downloaded < 0) throw new IllegalArgumentException("downloaded cannot be negative");
        if (uploaded < 0) throw new IllegalArgumentException("uploaded cannot be negative");
        if (left < 0) throw new IllegalArgumentException("left cannot be negative");
        if (redundant < 0) throw new IllegalArgumentException("redundant cannot be negative");
        if (port < 0) throw new IllegalArgumentException("port cannot be negative");
        if (numWant < 0) throw new IllegalArgumentException("numWant cannot be negative");
    }

    public AnnounceData toAnnounceData() {
        return new AnnounceData(userAgent, List.of(ipv4), List.of(ipv6), List.of(clientReportIps), IPUtil.getRequestIp(r), port, passkey, left, event, noPeerId, supportCrypto, compact, infoHash, downloaded, uploaded, redundant, peerId, numWant);
    }


    private String readMultipleAliasOne(String[] alias, @Nullable String def) {
        for (String s : alias) {
            if (q.containsKey(s)) {
                return q.get(s);
            }
        }
        return def;
    }

    private String[] readMultipleAlias(String[] alias, @Nullable String[] def) {
        for (String s : alias) {
            if (q.containsKey(s)) {
                return r.getParameterValues(s);
            }
        }
        return def;
    }

}
