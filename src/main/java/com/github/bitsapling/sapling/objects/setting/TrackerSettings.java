package com.github.bitsapling.sapling.objects.setting;

import java.util.List;

public class TrackerSettings {
    private boolean enableTracker;
    private List<String> trackerURL;
    private long maxTorrentSize;
    private long torrentIntervalMin;
    private long torrentIntervalMax;
    private boolean clientWhitelistMode;
    private List<String> clients;
    private boolean ipAddressWhitelistMode;
    private List<String> ips;
    private boolean portWhiteListMode;
    private List<Integer> ports;
    private String torrentPrefix;
}
