package com.github.bitsapling.sapling.module.mail.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class MailSendDTO {
    private final @NotNull Long owner;
    private final @NotNull Long sender;
    private final @NotEmpty String title;
    private final @NotEmpty String description;

    public MailSendDTO(@NotNull Long owner, @NotNull Long sender, @NotEmpty String title, @NotEmpty String description) {
        this.owner = owner;
        this.sender = sender;
        this.title = title;
        this.description = description;
    }
}
