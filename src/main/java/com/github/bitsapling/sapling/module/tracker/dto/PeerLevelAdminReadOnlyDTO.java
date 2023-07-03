package com.github.bitsapling.sapling.module.tracker.dto;

import com.github.bitsapling.sapling.module.tracker.Peer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Validated
public class PeerLevelAdminReadOnlyDTO extends Peer {
    public PeerLevelAdminReadOnlyDTO(@Positive Long id, @Positive Long torrent, @NotEmpty Byte[] peerId,
                                     @Positive Long user, @NotEmpty String ip, @Positive Integer port, @PositiveOrZero BigInteger uploaded,
                                     @PositiveOrZero BigInteger downloaded, @PositiveOrZero BigInteger toGo, @NotNull LocalDateTime startedAt,
                                     @NotNull LocalDateTime lastAction, @NotNull LocalDateTime prevAction, @NotEmpty String userAgent,
                                     @NotNull LocalDateTime finishedAt, @PositiveOrZero BigInteger downloadedOffset,
                                     @PositiveOrZero BigInteger uploadedOffset, Boolean connectable) {
        super(id, torrent, peerId, user, ip, port, uploaded, downloaded, toGo, startedAt, lastAction, prevAction, userAgent, finishedAt, downloadedOffset, uploadedOffset, connectable);
    }
}
