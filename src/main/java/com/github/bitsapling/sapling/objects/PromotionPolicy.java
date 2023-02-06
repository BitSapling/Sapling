package com.github.bitsapling.sapling.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PromotionPolicy implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private long id;
    private String displayName;
    private double uploadRatio;
    private double downloadRatio;

    public long applyUploadRatio(double upload) {
        return (long) (upload * uploadRatio);
    }

    public long applyDownloadRatio(double download) {
        return (long) (download * downloadRatio);
    }
}
