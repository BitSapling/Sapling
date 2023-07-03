package com.github.bitsapling.sapling.module.user.dto;

import com.github.bitsapling.sapling.module.user.UserMetadata;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.BigInteger;

@EqualsAndHashCode(callSuper = true)
@Validated
public class UserMetadataDTO extends UserMetadata {
    public UserMetadataDTO(@Positive Long id, @Positive Long user, @NotNull BigInteger downloaded, @NotNull BigInteger uploaded,
                           @NotNull BigInteger realDownloaded, @NotNull BigInteger realUploaded, @NotNull BigDecimal karma,
                           @NotNull BigInteger totalSeedingTime, @NotNull BigInteger totalDownloadingTime) {
        super(id, user, downloaded, uploaded, realDownloaded, realUploaded, karma, totalSeedingTime, totalDownloadingTime);
    }
}
