package com.github.bitsapling.sapling.module.torrent.dto;

import com.github.bitsapling.sapling.module.torrent.Torrent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Validated
public class TorrentDTO extends Torrent {

    public TorrentDTO(@PositiveOrZero Long id, String infoHashV1, String infoHashV2, @Positive Long uploader,
                      @Positive Long fileId, @NotNull Boolean isReview, @NotNull Boolean isBanned,
                      @NotNull Boolean isDraft, @NotNull LocalDateTime addedAt, @NotNull Boolean
                              anonymous, @NotEmpty String title, @NotEmpty String subTitle,
                      @Positive Long category, @NotEmpty String description, @Positive Long promotion,
                      @NotNull LocalDateTime promotionEndAt) {
        super(id, infoHashV1, infoHashV2, uploader, fileId, isReview, isBanned, isDraft, addedAt, anonymous, title, subTitle, category, description, promotion, promotionEndAt);
    }
}
