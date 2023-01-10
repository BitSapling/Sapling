package com.github.bitsapling.sapling.controller.announce;

import com.github.bitsapling.sapling.exception.TrackerException;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Translated from <a href="https://github.com/swetorrentking/rartracker/blob/master/tracker.php">here</a>
 * <p>
 * Thanks for the WTFPL :)
 */
@Controller
public class AnnounceController {
    private static final Random random = new Random();

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

    private static final Pattern infoHashPattern = Pattern.compile("info_hash=(.*?)($|&)");
    private static final Pattern peerIdPattern = Pattern.compile("peer_id=(.*?)&");

    public AnnounceController() {
    }

    @GetMapping("/{action}/{passkey}")
    public void announce(@PathVariable String action, @PathVariable String passkey, @RequestParam Map<String, String> gets) throws TrackerException {
        String infoHash = gets.getOrDefault("info_hash", "");
        String peerId = gets.getOrDefault("peer_id", "");
        int left = Integer.parseInt(gets.getOrDefault("left", "-1"));
        var setting = new Setting();
        var start = timeOfDay();
        /*var keys = requestUri.split("/");
        if (keys.length < 3 || keys[2].length() != setting.passkeyLength || keys[2].matches("^[a-zA-Z0-9]*$")) {
            throw new TrackerException("Invalid passkey. Re-download torrent!");
        }*/
        if (infoHash.length() < 4) {
            var matcher = infoHashPattern.matcher(infoHash);
            if (matcher.find()) {
                infoHash = URLDecoder.decode(matcher.group(1), StandardCharsets.UTF_8);
            }
        }
        if (peerId.length() < 4) {
            var matcher = peerIdPattern.matcher(peerId);
            if (matcher.matches()) {
                peerId = URLDecoder.decode(matcher.group(1), StandardCharsets.UTF_8);
            }
        }
        if (action.contains("announce")) {
            peerId = hasheval(peerId, 20, "peer_id");
            var seeder = left == 0 ? "yes" : "no";
            var vars = new String[]{
                    "port",
                    "uploaded",
                    "downloaded",
                    "left"
            };
            for (var var : vars) {
                if (!gets.containsKey(var) || !gets.get(var).matches("^[a-zA-Z0-9]*$")) {
                    throw new TrackerException("Invalid key: " + var + ".");
                }
            }
            var port = -1;
            try {
                port = Integer.parseInt(gets.get("port"));
                if (port > 0xffff || port < 1)
                    throw new TrackerException("Invalid port number: " + port + ".");
            } catch (Throwable e) {
                throw new TrackerException("Invalid port number.", e);
            }
            var ip = getIp();
            var optionalVars = new String[]{
                    "numwant",
                    "compact",
                    "no_peer_id"
            };
            for (var var : optionalVars) {
                if (gets.containsKey(var) && !gets.get(var).matches("^[0-9]*$")) {
                    throw new TrackerException("Invalid opt key: " + var + ".");
                }
            }
            var event = "";
            if (gets.containsKey("event")) {
                event = gets.get("event");
                if (!event.matches("^[a-zA-Z]*$")) {
                    throw new TrackerException("Invalid event.");
                }
                var events = Arrays.asList(
                        "started",
                        "stopped",
                        "completed",
                        "paused");
                if (!events.contains(event)) {
                    throw new TrackerException("Invalid event.");
                }
            }
            if (!setting.allowOldProtocols) {
                if (!gets.containsKey("compact") && (!event.equals("stopped") && !event.equals("completed"))) {
                    // client has not stopped or completed - should say it supports compact if doing so
                    throw new TrackerException("Please upgrade or change client.");
                }
            }
            var peers = findPeer(infoHash, port, ip);
            if (peers.size() == 0) { // peer not found - insert into database, but only if not event=stopped
                if (setting.logDebug) {
                    debuglog("announce: peer not found!");
                }
                if (event.equals("stopped")) {
                    throw new TrackerException("Client sent stop, but peer not found!");
                }
                var user = gatherUser(passkey).orElseThrow(() -> new TrackerException("Permission denied."));
                var torrent = gatherTorrent(infoHash).orElseThrow(() -> new TrackerException("Torrent does not exist on this tracker."));

                var timesCompleted = false;
                var timesStarted = false;
                var timesUpdated = false;
                if (event.equals("completed") && left == 0) {
                    timesCompleted = true;
                } else if (event.equals("started")) {// && left == 0) { //TODO: start size check
                    timesStarted = true;
                } else {
                    timesUpdated = true;
                }
                // TODO: update torrent status
                if (seeder.equals("yes")) {
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
        } else if (action.contains("scrape")) {
            sendBencode();
        } else {
            throw new TrackerException("Unknown action.");
        }
    }

    public void sendBencode() {
        // TODO
    }

    public void give_peers() {
        // TODO
    }

    public boolean connectable(String ip, int port) {
        return false;
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

    public Set<Object> findPeer(String infoHash, int port, String ip) {
        // TODO
        return new HashSet<>();
    }

    public long timeOfDay() {
        return System.nanoTime() / 1000;
    }

    public long time() {
        return System.currentTimeMillis() / 1000;
    }

    public String hasheval(String str, int len, String name) {
        // TODO
        return "";
    }

    public String getIp() {
        // TODO
        return "0.0.0.0";
    }
}
