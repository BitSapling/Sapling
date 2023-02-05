package com.github.bitsapling.sapling.objects.setting;

import com.github.bitsapling.sapling.type.GuestAccessBlocker;
import com.github.bitsapling.sapling.type.GuestAccessRequirement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SecuritySettings implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int maxIp;
    private int maxLoginAttempts;
    private int maxAnnounceInvalidKeyAttempt;
    private GuestAccessBlocker guestAccessBlocker;
    private boolean guestAccessRequirementAnyMode;
    private List<GuestAccessRequirement> guestAccessRequirement;
    private List<String> guestAccessSecret;
    private List<String> guestAccessReferer;
    private List<String> guestAccessUserAgentKeyword;
    private List<String> guestAccessIp;
}
