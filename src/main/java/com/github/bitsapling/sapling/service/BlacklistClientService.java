package com.github.bitsapling.sapling.service;

import com.github.bitsapling.sapling.exception.BrowserReadableAnnounceException;
import com.github.bitsapling.sapling.exception.FixedAnnounceException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Repository
public class BlacklistClientService {
    private static final String[] BROWSER_BOT_SOFTWARE_KEYWORDS = new String[]{
            "Mozilla",
            "Browser",
            "Chrome",
            "Safari",
            "AppleWebKit",
            "Opera",
            "Links",
            "Lynx",
            "Bot",
            "Crawler",
            "Spider",
            "Unknown"
    };
    public void checkClient(@NotNull HttpServletRequest request) throws FixedAnnounceException, BrowserReadableAnnounceException {
        String ua = request.getHeader("User-Agent");
        if(StringUtils.isEmpty(ua)){
            throw new FixedAnnounceException("Client didn't send user-agent to tracker server.");
        }
        checkBrowser(ua);
        if(!checkAllowedClient(ua)){
            throw new FixedAnnounceException("Disallowed client: "+ua);
        }
    }

    private boolean checkAllowedClient(@NotNull String ua) {
        return true;
    }

    private void checkBrowser(@NotNull String ua) throws BrowserReadableAnnounceException {
        if(Arrays.stream(BROWSER_BOT_SOFTWARE_KEYWORDS).map(String::toLowerCase).anyMatch(ua::contains)){
            throw new BrowserReadableAnnounceException("You must use a Bittorrent Client to connect this tracker.");
        }
    }
}
