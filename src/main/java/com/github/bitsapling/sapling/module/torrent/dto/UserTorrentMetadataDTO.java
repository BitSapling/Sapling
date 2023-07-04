package com.github.bitsapling.sapling.module.torrent.dto;

import com.github.bitsapling.sapling.module.torrent.UserTorrentMetadata;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Validated
public class UserTorrentMetadataDTO extends UserTorrentMetadata {

    public UserTorrentMetadataDTO(@PositiveOrZero Long id, @Positive Long user, @Positive Long torrent,
                                  @NotNull BigInteger downloaded,
                                  @NotNull BigInteger uploaded, @NotNull BigInteger realDownloaded,
                                  @NotNull BigInteger realUploaded, @NotNull BigInteger seedingTime,
                                  @NotNull BigInteger downloadingTime) {
        super(id, user, torrent, downloaded, uploaded, realDownloaded, realUploaded, seedingTime, downloadingTime);
    }
}
