package com.github.bitsapling.sapling.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScrapeContainerDTO {
    private long downloaded;
    private long complete;
    private long incomplete;
    private long downloaders;
}
