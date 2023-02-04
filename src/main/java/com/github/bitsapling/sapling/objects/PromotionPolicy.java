package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PromotionPolicy {
    private final long id;
    private String displayName;
    private double uploadRatio;
    private double downloadRatio;

    public long applyUploadRatio(double upload){
        return (long)(upload * uploadRatio);
    }
    public long applyDownloadRatio(double download){
        return (long)(download * downloadRatio);
    }
}
