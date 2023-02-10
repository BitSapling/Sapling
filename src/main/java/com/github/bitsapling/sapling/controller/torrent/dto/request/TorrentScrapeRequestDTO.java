package com.github.bitsapling.sapling.controller.torrent.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TorrentScrapeRequestDTO {
    @NotNull
    private List<String> torrents;
    @NotNull
    private boolean details;
}
