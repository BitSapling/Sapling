package com.ghostchu.sapling.service;

import org.springframework.stereotype.Service;

@Service
public class SeedBoxService {
    public boolean isSeedBox(String ip) {
        return false;
    }

    public boolean isSeedBoxSkipPromotion() {
        return true;
    }
}
