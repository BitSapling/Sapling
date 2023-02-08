package com.github.bitsapling.sapling.config;

import com.github.bitsapling.sapling.type.GuestAccessBlocker;
import com.github.bitsapling.sapling.type.GuestAccessRequirement;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
@Data
@AllArgsConstructor
public class SecurityConfig {
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

    @NotNull
    public static String getConfigKey(){
        return "security";
    }

}
