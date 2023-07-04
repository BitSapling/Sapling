package com.github.bitsapling.sapling.module.uacontrol.dto;

import com.github.bitsapling.sapling.module.uacontrol.BtClientUA;
import com.github.bitsapling.sapling.module.uacontrol.BtClientUAMatchType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@EqualsAndHashCode(callSuper = true)
@Validated
public class BtClientUADTO extends BtClientUA {
    public BtClientUADTO(@PositiveOrZero Long id, @NotEmpty String userAgent, @NotNull BtClientUAMatchType matchType, @NotNull Boolean enabled, @NotEmpty String description) {
        super(id, userAgent, matchType, enabled, description);
    }
}
