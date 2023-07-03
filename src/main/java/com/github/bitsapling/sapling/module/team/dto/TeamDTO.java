package com.github.bitsapling.sapling.module.team.dto;

import com.github.bitsapling.sapling.module.team.Team;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

@EqualsAndHashCode(callSuper = true)
@Validated
public class TeamDTO extends Team {
    public TeamDTO(@PositiveOrZero Long id, @NotEmpty String name, @NotEmpty String permissionName) {
        super(id, name, permissionName);
    }
}
