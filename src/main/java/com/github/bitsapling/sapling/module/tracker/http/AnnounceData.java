package com.github.bitsapling.sapling.module.tracker.http;

import com.github.bitsapling.sapling.module.tracker.AnnounceEventType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnnounceData {
    private final String userAgent;
    private final List<String> ipv4;
    private final List<String> ipv6;
    private final List<String> clientReportIps;
    private final String clientRequestIp;
    private final List<String> clientAllIps = new ArrayList<>();
    private final int port;
    private final String passkey;
    private final long left;
    private final AnnounceEventType event;
    private final boolean noPeerId;
    private final boolean supportCrypto;
    private final boolean compact;
    private final String infoHash;
    private final long downloaded;
    private final long uploaded;
    private final int redundant;
    private final String peerId;
    private final int numWant;

    public AnnounceData(String userAgent, List<String> ipv4, List<String> ipv6, List<String> clientReportIps, String clientRequestIp, int port, String passkey, long left, String event, boolean noPeerId, boolean supportCrypto, boolean compact, String infoHash, long downloaded, long uploaded, int redundant, String peerId, int numWant) {
        this.userAgent = userAgent;
        this.ipv4 = ipv4;
        this.ipv6 = ipv6;
        this.clientReportIps = clientReportIps;
        this.clientRequestIp = clientRequestIp;
        this.clientAllIps.addAll(ipv4);
        this.clientAllIps.addAll(ipv6);
        this.clientAllIps.addAll(clientReportIps);
        this.clientAllIps.add(clientRequestIp);
        this.port = port;
        this.passkey = passkey;
        this.left = left;
        this.event = AnnounceEventType.fromName(event.toUpperCase());
        this.noPeerId = noPeerId;
        this.supportCrypto = supportCrypto;
        this.compact = compact;
        this.infoHash = infoHash;
        this.downloaded = downloaded;
        this.uploaded = uploaded;
        this.redundant = redundant;
        this.peerId = peerId;
        this.numWant = numWant;
        if (userAgent == null && peerId == null) {
            throw new IllegalArgumentException("User agent and peer id cannot both be null");
        }
    }
}
