package com.github.bitsapling.sapling.module.announcement.dto;

import com.github.bitsapling.sapling.module.announcement.Announcement;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Validated
public class AnnouncementDTO extends Announcement {
    public AnnouncementDTO(@PositiveOrZero Long id, @PositiveOrZero Long owner, LocalDateTime addedAt, @Future LocalDateTime endedAt, @NotEmpty String title, @NotEmpty String content) {
        super(id, owner, addedAt, endedAt, title, content);
    }
}
