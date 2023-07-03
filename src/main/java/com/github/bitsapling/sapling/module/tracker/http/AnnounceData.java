package com.github.bitsapling.sapling.module.tracker.http;

import com.github.bitsapling.sapling.module.tracker.AnnounceEventType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnnounceData {
    private String userAgent;
    private List<String> ipv4;
    private List<String> ipv6;
    private List<String> clientReportIps;
    private String clientRequestIp;
    private List<String> clientAllIps = new ArrayList<>();
    private int port;
    private String passkey;
    private long left;
    private AnnounceEventType event;
    private boolean noPeerId;
    private boolean supportCrypto;
    private boolean compact;
    private String infoHash;
    private long downloaded;
    private long uploaded;
    private int redundant;
    private String peerId;
    private int numWant;

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
