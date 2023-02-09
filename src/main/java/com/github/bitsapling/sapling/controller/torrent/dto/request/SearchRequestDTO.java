package com.github.bitsapling.sapling.controller.torrent.dto.request;

import lombok.Data;

import java.util.List;
@Data
public class SearchRequestDTO {
    private String keyword;
    private List<Long> promotion;
    private List<String> category;
    private boolean includeDeadTorrent;
}
