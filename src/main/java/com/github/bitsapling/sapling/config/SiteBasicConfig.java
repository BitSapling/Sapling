package com.github.bitsapling.sapling.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@AllArgsConstructor
public class SiteBasicConfig {
    private String siteName;
    private String siteSubName;
    private String siteBaseURL;
    private String siteDescription;
    private List<String> siteKeywords;
    private boolean openRegistration;
    private boolean maintenanceMode;
    @NotNull
    public static String getConfigKey(){
        return "site_basic";
    }
    @NotNull
    public static SiteBasicConfig spawnDefault(){
        return new SiteBasicConfig(
                "Another Sapling Site",
                "未配置的站点",
                "https://www.example.com",
                "又一个由 Sapling 驱动的站点！",
                List.of("BitTorrent", "Torrent", "File Sharing", "Private Tracker"),
                false,
                false);
    }
}
