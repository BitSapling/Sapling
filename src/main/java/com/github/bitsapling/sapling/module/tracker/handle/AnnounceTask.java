package com.github.bitsapling.sapling.module.tracker.handle;

import com.github.bitsapling.sapling.module.tracker.http.AnnounceData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class AnnounceTask implements Serializable {
    private final AnnounceData announceData;
    private final long userId;
    private final long torrentId;
}
