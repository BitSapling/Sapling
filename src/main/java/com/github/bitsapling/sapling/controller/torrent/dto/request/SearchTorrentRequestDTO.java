package com.github.bitsapling.sapling.controller.torrent.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class SearchTorrentRequestDTO {
    private String keyword;
    private List<String> promotion;
    private List<String> category;

    private List<String> tag;
    private boolean includeDeadTorrent;
    @PositiveOrZero
    private int page;
    @Min(1)
    @Max(300)
    private int entriesPerPage;
}
