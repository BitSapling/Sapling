package com.github.bitsapling.sapling.module.mail.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Valid
@AllArgsConstructor
@EqualsAndHashCode
public class DraftedMail {
    private final @PositiveOrZero Long receiver;
    private final @NotEmpty String title;
    private final @NotEmpty String description;
}
