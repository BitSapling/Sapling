package com.github.bitsapling.sapling.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
@Data
@AllArgsConstructor
public class TrackerConfig {
    private List<String> trackerURL;
    private long maxTorrentSize;
    private int torrentIntervalMin;
    private int torrentIntervalMax;
    private boolean ipAddressWhitelistMode;
    private List<String> controlIps;
    private boolean portWhiteListMode;
    private List<Integer> controlPorts;
    private String torrentPrefix;

    @NotNull
    public static String getConfigKey(){
        return "tracker";
    }

    @NotNull
    public static TrackerConfig spawnDefault(){
        return new TrackerConfig(
                List.of(
                        "https://tracker1.example.com/announce",
                        "https://tracker2.example.com/announce"
                ),
                -1,
                60 * 60 * 15,
                60 * 60 * 45,
                false,
                List.of(),
                false,
                List.of(20,21,22,23,25,80,110,119,161,162,443,445,1433,1521,2049,3306,3389,8080,8081),
                "Sapling");
    }
}
