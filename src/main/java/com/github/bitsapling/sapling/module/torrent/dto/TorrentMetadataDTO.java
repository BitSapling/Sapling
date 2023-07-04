package com.github.bitsapling.sapling.module.torrent.dto;

import com.github.bitsapling.sapling.module.torrent.TorrentMetadata;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@EqualsAndHashCode(callSuper = true)
@Validated
public class TorrentMetadataDTO extends TorrentMetadata {

    public TorrentMetadataDTO(@PositiveOrZero Long id, @Positive Long torrent,
                              @PositiveOrZero Long seeders, @PositiveOrZero Long leechers,
                              @PositiveOrZero Long timesCompleted, @PositiveOrZero Long timesFileDownloaded) {
        super(id, torrent, seeders, leechers, timesCompleted, timesFileDownloaded);
    }
}
