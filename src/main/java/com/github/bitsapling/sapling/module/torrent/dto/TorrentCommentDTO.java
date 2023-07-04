package com.github.bitsapling.sapling.module.torrent.dto;

import com.github.bitsapling.sapling.module.torrent.TorrentComment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Validated
public class TorrentCommentDTO extends TorrentComment {
    public TorrentCommentDTO(@PositiveOrZero Long id, @Positive Long torrent, @Positive Long owner,
                             @Positive Long replyTo, @NotNull LocalDateTime createdAt,
                             LocalDateTime editedAt, @NotEmpty String description) {
        super(id, torrent, owner, replyTo, createdAt, editedAt, description);
    }
}
