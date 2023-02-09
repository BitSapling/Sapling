package com.github.bitsapling.sapling.controller.torrent.dto.request;

import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchTorrentRequestDTO extends ResponsePojo {
    private String keyword;
    private List<String> promotion;
    private List<String> category;
    private boolean includeDeadTorrent;
    private int page;
    private int entriesPerPage;
}
