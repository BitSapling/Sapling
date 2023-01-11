package com.github.bitsapling.sapling.controller.announce;

import com.github.bitsapling.sapling.exception.FixedAnnounceException;
import com.github.bitsapling.sapling.exception.InvalidAnnounceException;
import com.github.bitsapling.sapling.exception.TrackerException;
import com.github.bitsapling.sapling.model.BlacklistClient;
import com.github.bitsapling.sapling.type.AnnounceEventType;
import com.github.bitsapling.sapling.util.BooleanUtil;
import com.github.bitsapling.sapling.util.MiscUtil;
import com.github.bitsapling.sapling.util.SafeUUID;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Translated from <a href="https://github.com/swetorrentking/rartracker/blob/master/tracker.php">here</a>
 * <p>
 * Thanks for the WTFPL :)
 */
@Controller
@Slf4j
public class AnnounceController {
    private static final Random random = new Random();
    private static final Pattern infoHashPattern = Pattern.compile("info_hash=(.*?)($|&)");
    private static final Pattern peerIdPattern = Pattern.compile("peer_id=(.*?)&");
    private final HttpServletRequest request;
    private final BlacklistClient blacklistClient;

    public AnnounceController(@Autowired HttpServletRequest request, @Autowired BlacklistClient blacklistClient) {
        this.request = request;
        this.blacklistClient = blacklistClient;
    }

    @GetMapping("/scrape/{passkey}")
    public void scrape(@PathVariable String passkey, @RequestParam List<String> infoHashes) {
        // TODO
    }

    @GetMapping("/announce/{passkey}")
    public void announce(@PathVariable String passkey, @RequestParam Map<String, String> gets) throws TrackerException, FixedAnnounceException {
        long start = timeOfDay();

        if (StringUtils.isEmpty(passkey)) {
            throw new InvalidAnnounceException("You must re-download the torrent from tracker for seeding.");
        }

        if (!SafeUUID.isDashesStrippedUUID(passkey)) {
            throw new InvalidAnnounceException("Invalid passkey.");
        }

        checkClient();

        Setting setting = getSettings();

        checkAnnounceFields(gets);
        String peerId = gets.get("peer_id");
        //peerId = hasheval(peerId, 20, "peer_id");
        long left = Long.parseLong(gets.get("left"));
        boolean seeder = left == 0;
        int port = Integer.parseInt(gets.get("port"));
        AnnounceEventType event = AnnounceEventType.valueOf(gets.get("event"));
        int numWant = Integer.parseInt(Optional.ofNullable(MiscUtil.anyNotNull(gets.get("numwant"), gets.get("num want"), gets.get("num_want"))).orElse("150"));
        boolean noPeerId = BooleanUtil.parseBoolean(Optional.ofNullable(MiscUtil.anyNotNull(gets.get("nopeerid"), gets.get("no_peerid"), gets.get("no_peer_id"))).orElse("0"));
        boolean supportCrypto = BooleanUtil.parseBoolean(Optional.ofNullable(MiscUtil.anyNotNull(gets.get("supportcrypto"), gets.get("support crypto"), gets.get("support_crypto"))).orElse("0"));
        boolean compact = BooleanUtil.parseBoolean(gets.get("compact"));
        String peerIp = Optional.ofNullable(MiscUtil.anyNotNull(gets.get("ip"), gets.get("address"), gets.get("ipaddress"), gets.get("ip_address"), gets.get("ip address"))).orElse(request.getRemoteAddr());
        String infoHash = gets.get("info_hash");

        var peers = findPeer(infoHash, port, peerIp);
        if (peers.size() == 0) { // peer not found - insert into database, but only if not event=stopped
            if (setting.logDebug) {
                debuglog("announce: peer not found!");
            }
            if (event == AnnounceEventType.STOPPED) {
                throw new TrackerException("Client sent stop, but peer not found!");
            }
            var user = gatherUser(passkey).orElseThrow(() -> new TrackerException("Permission denied."));
            var torrent = gatherTorrent(infoHash).orElseThrow(() -> new TrackerException("Torrent does not exist on this tracker."));

            var timesCompleted = false;
            var timesStarted = false;
            var timesUpdated = false;
            if (event == AnnounceEventType.COMPLETED && left == 0) {
                timesCompleted = true;
            } else if (event == AnnounceEventType.STARTED) {// && left == 0) { //TODO: start size check
                timesStarted = true;
            } else {
                timesUpdated = true;
            }
            // TODO: update torrent status
            if (seeder) {
                // TODO: update seeder status
            } else {
                // TODO: update leecher status
            }
        } else if (peers.size() == 1) {
            if (setting.rateLimitation) {
                // TODO: rate detection
            }
            if (setting.registerStats) { // TODO: check if there is changes

            }
            give_peers();
        } else {
            // we hit multiple? but that's IMPOSSIBLE! ;)
            if (setting.logDebug) {
                debuglog("announce: got multiple targets in peer table!");
            }
            throw new TrackerException("Got multiple targets in peer table!");
        }

        if (setting.timeMe && setting.logDebug) {
            debuglog("announce: " + (timeOfDay() - start) + " us");
        }
    }

    private void checkClient() throws InvalidAnnounceException {
        String method = request.getMethod();
        if (!method.equals("GET")) {
            throw new InvalidAnnounceException("Invalid request method: " + method);
        }
        String userAgent = request.getHeader("User-Agent");
        if (blacklistClient.isBanned(userAgent)) {
            throw new InvalidAnnounceException("Banned client: " + userAgent);
        }
    }

    private void checkAnnounceFields(@NotNull Map<String, String> gets) throws InvalidAnnounceException {
        if (StringUtils.isEmpty(gets.get("info_hash")))
            throw new InvalidAnnounceException("Missing param: info_hash");
        if (StringUtils.isEmpty(gets.get("peer_id")))
            throw new InvalidAnnounceException("Missing param: peer_id");
        if (StringUtils.isEmpty(gets.get("port")) || !StringUtils.isNumeric(gets.get("port")))
            throw new InvalidAnnounceException("Missing/Invalid param: port");
        if (StringUtils.isEmpty(gets.get("uploaded")) || !StringUtils.isNumeric(gets.get("uploaded")))
            throw new InvalidAnnounceException("Missing/Invalid param: uploaded");
        if (StringUtils.isEmpty(gets.get("downloaded")) || !StringUtils.isNumeric(gets.get("downloaded")))
            throw new InvalidAnnounceException("Missing/Invalid param: downloaded");
        if (StringUtils.isEmpty(gets.get("left")) || !StringUtils.isNumeric(gets.get("left")))
            throw new InvalidAnnounceException("Missing/Invalid param: left");
        if (StringUtils.isEmpty(gets.get("event")))
            throw new InvalidAnnounceException("Missing param: event; Please update or change your client.");
        if (AnnounceEventType.fromName(gets.get("event")) == null)
            throw new InvalidAnnounceException("Invalid param: event");
        String numwant = MiscUtil.anyNotNull(gets.get("numwant"), gets.get("num want"), gets.get("num_want"));
        if (!StringUtils.isEmpty(numwant) && !StringUtils.isNumeric(numwant)) {
            throw new InvalidAnnounceException("Invalid optional param: numwant");
        }
        if (!StringUtils.isEmpty(gets.get("compact")) && !StringUtils.isNumeric(gets.get("compact"))) {
            throw new InvalidAnnounceException("Invalid optional param: compact");
        }
        if (!StringUtils.isEmpty(gets.get("corrupt")) && !StringUtils.isNumeric(gets.get("corrupt"))) {
            throw new InvalidAnnounceException("Invalid optional param: corrupt");
        }

    }

    public Set<Object> findPeer(String infoHash, int port, String ip) {
        // TODO
        return new HashSet<>();
    }

    public void debuglog(String log) {
        // TODO
    }

    public Optional<Object> gatherUser(String passkey) {
        // TODO
        return Optional.empty();
    }

    public Optional<Object> gatherTorrent(String infoHash) {
        // TODO
        return Optional.empty();
    }

    public void give_peers() {
        // TODO
    }

    public long timeOfDay() {
        return System.nanoTime() / 1000;
    }

    public void sendBencode() {
        // TODO
    }

    public String hasheval(String str, int len, String name) {
        // TODO
        return "";
    }

    public String getIp() {
        // TODO
        return "0.0.0.0";
    }

    public boolean connectable(String ip, int port) {
        return false;
    }

    public long time() {
        return System.currentTimeMillis() / 1000;
    }

    public Setting getSettings() {
        // TODO
        return new Setting();
    }

    @Data
    public static class Setting {
        boolean timeMe = false; // calculate execution times (requires log_debug)
        boolean logDebug = false; // log debugging information using debuglog()
        boolean logErrors = false; // log all errors sent using err()
        String timestampFormat = "[d/m/y H:i:s]";
        String logFile = "/var/www/debug.txt";
        boolean gzip = true; // gzip the data sent to the clients
        boolean allowOldProtocols = true; // allow no_peer_id and original protocols for compatibility
        boolean allowGlobalScrape = false; // enable scrape-statistics for all torrents if no info_hash specified - wastes bandwidth on big trackers
        int defaultGivePeers = 50; // how many peers to give to client by default
        int maxGivePeers = 150; // maximum peers client may request
        int announceInterval = random.nextInt(1800, 2400); // 30-40 min - spread load a bit on the webserver
        boolean rateLimitation = true; // calculate the clients average upload-speed
        int rateLimitationWarnUp = 2; // log a warning if exceeding this amount of MB/s
        int rateLimitationErrUp = 60; // log an error and don't save stats for user if exceeding this amount of MB/s
        boolean registerStats = true; // save transfer statistics for the users? [0-1 extra mysql-queries]
        int uploadMultiplier = 1;
        int downloadMultiplier = 1;
        int passkeyLength = 32;
    }
}
