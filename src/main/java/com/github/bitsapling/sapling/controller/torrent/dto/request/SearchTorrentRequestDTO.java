package com.github.bitsapling.sapling.controller.torrent.dto.request;

import com.github.bitsapling.sapling.objects.ResponsePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchTorrentRequestDTO extends ResponsePojo {
    private String keyword;
    private List<String> promotion;
    private List<String> category;
    private List<String> tag;
    private boolean includeDeadTorrent;
    private int page;
    private int entriesPerPage;
}
