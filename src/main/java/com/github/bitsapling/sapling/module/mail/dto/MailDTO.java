package com.github.bitsapling.sapling.module.mail.dto;

import com.github.bitsapling.sapling.module.mail.Mail;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
public class MailDTO extends Mail {

    public MailDTO(@PositiveOrZero Long id, @NotNull Long owner, @NotNull Long sender, String senderName,
                   @NotEmpty String title, @NotEmpty String description,
                   LocalDateTime createdAt, LocalDateTime readedAt, LocalDateTime deletedAt) {
        super(id, owner, sender, senderName, title, description, createdAt, readedAt, deletedAt);
    }
}
