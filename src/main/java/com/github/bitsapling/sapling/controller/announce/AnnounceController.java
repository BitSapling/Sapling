package com.github.bitsapling.sapling.controller.announce;

import com.dampcake.bencode.Bencode;
import com.github.bitsapling.sapling.entity.Peer;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.exception.AnnounceBusyException;
import com.github.bitsapling.sapling.exception.BrowserReadableAnnounceException;
import com.github.bitsapling.sapling.exception.FixedAnnounceException;
import com.github.bitsapling.sapling.exception.InvalidAnnounceException;
import com.github.bitsapling.sapling.repository.UserRepository;
import com.github.bitsapling.sapling.service.AnnounceService;
import com.github.bitsapling.sapling.service.BlacklistClientService;
import com.github.bitsapling.sapling.service.PeerService;
import com.github.bitsapling.sapling.type.AnnounceEventType;
import com.github.bitsapling.sapling.util.BooleanUtil;
import com.github.bitsapling.sapling.util.MiscUtil;
import com.github.bitsapling.sapling.util.SafeUUID;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
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
    private static final Bencode BITTORRENT_STANDARD_BENCODE_ENCODER = new Bencode(StandardCharsets.ISO_8859_1);
    private static final InetAddressValidator ipValidator = InetAddressValidator.getInstance();
    private static final int MIN_INTERVAL = 60 * 60 * 15;
    private static final int MAX_INTERVAL = 60 * 60 * 45;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private BlacklistClientService blacklistClientService;
    @Autowired
    private PeerService peerService;
    @Autowired
    private AnnounceService announceBackgroundJob;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/scrape/{passkey}")
    public void scrape(@PathVariable String passkey, @RequestParam List<String> infoHashes) {
        // TODO
    }

    @GetMapping("/announce/{passkey}")
    public String announce(@PathVariable String passkey, @RequestParam Map<String, String> gets) throws FixedAnnounceException, BrowserReadableAnnounceException, AnnounceBusyException {
        long start = timeOfDay();

        if (StringUtils.isEmpty(passkey)) {
            throw new InvalidAnnounceException("You must re-download the torrent from tracker for seeding.");
        }

        if (!SafeUUID.isDashesStrippedUUID(passkey)) {
            throw new InvalidAnnounceException("Invalid passkey.");
        }

        checkClient();
        checkAnnounceFields(gets);
        String peerId = gets.get("peer_id");
        //String peerIdHash = Hashing.murmur3_128().hashString(peerId, StandardCharsets.UTF_8).toString();
        long left = Long.parseLong(gets.get("left"));
        int port = Integer.parseInt(gets.get("port"));
        AnnounceEventType event = AnnounceEventType.valueOf(gets.get("event"));
        int numWant = Integer.parseInt(Optional.ofNullable(MiscUtil.anyNotNull(gets.get("numwant"), gets.get("num want"), gets.get("num_want"))).orElse("150"));
        numWant = Math.min(numWant, 300);
        boolean noPeerId = BooleanUtil.parseBoolean(Optional.ofNullable(MiscUtil.anyNotNull(gets.get("nopeerid"), gets.get("no_peerid"), gets.get("no_peer_id"))).orElse("0"));
        boolean supportCrypto = BooleanUtil.parseBoolean(Optional.ofNullable(MiscUtil.anyNotNull(gets.get("supportcrypto"), gets.get("support crypto"), gets.get("support_crypto"))).orElse("0"));
        boolean compact = BooleanUtil.parseBoolean(gets.get("compact"));
        String peerIp = Optional.ofNullable(MiscUtil.anyNotNull(gets.get("ip"), gets.get("address"), gets.get("ipaddress"), gets.get("ip_address"), gets.get("ip address"))).orElse(request.getRemoteAddr());
        String infoHash = gets.get("info_hash");
        long downloaded = Math.max(0, Long.parseLong(gets.get("downloaded")));
        long uploaded = Math.max(0, Long.parseLong(gets.get("uploaded")));
        int redundant = Integer.parseInt(Optional.ofNullable(MiscUtil.anyNotNull(gets.get("redundant"), gets.get("redundant_peers"), gets.get("redundant peers"), gets.get("redundant_peers"))).orElse("0"));

        // User permission checks
        User user = userRepository.findByPasskey(passkey).orElseThrow(() -> new InvalidAnnounceException("Unauthorized"));
        if (!user.getGroup().hasPermission("torrent:announce")) {
            throw new InvalidAnnounceException("Permission Denied");
        }
        // User had permission to announce torrents
        // Create an announce tasks and drop into background, end this request as fast as possible
        announceBackgroundJob.schedule(new AnnounceService.AnnounceTask(peerIp, port, infoHash, peerId, uploaded, downloaded, left, event, numWant, user, compact, noPeerId, supportCrypto, redundant,request.getHeader("User-Agent")));
        return generatePeersResponse(peerId, infoHash, numWant, compact);
        // TODO check whether user have permission to download

//
//        if (peers.size() == 0) { // peer not found - insert into database, but only if not event=stopped
//            if (setting.logDebug) {
//                debuglog("announce: peer not found!");
//            }
//            if (event == AnnounceEventType.STOPPED) {
//                throw new TrackerException("Client sent stop, but peer not found!");
//            }
//            var user = gatherUser(passkey).orElseThrow(() -> new TrackerException("Permission denied."));
//            var torrent = gatherTorrent(infoHash).orElseThrow(() -> new TrackerException("Torrent does not exist on this tracker."));
//
//            var timesCompleted = false;
//            var timesStarted = false;
//            var timesUpdated = false;
//            if (event == AnnounceEventType.COMPLETED && left == 0) {
//                timesCompleted = true;
//            } else if (event == AnnounceEventType.STARTED) {// && left == 0) { //TODO: start size check
//                timesStarted = true;
//            } else {
//                timesUpdated = true;
//            }
//            // TODO: update torrent status
//            if (seeder) {
//                // TODO: update seeder status
//            } else {
//                // TODO: update leecher status
//            }
//        } else if (peers.size() == 1) {
//            if (setting.rateLimitation) {
//                // TODO: rate detection
//            }
//            if (setting.registerStats) { // TODO: check if there is changes
//
//            }
//            give_peers();
//        } else {
//            // we hit multiple? but that's IMPOSSIBLE! ;)
//            if (setting.logDebug) {
//                debuglog("announce: got multiple targets in peer table!");
//            }
//            throw new TrackerException("Got multiple targets in peer table!");
//        }
//
//        if (setting.timeMe && setting.logDebug) {
//            debuglog("announce: " + (timeOfDay() - start) + " us");
//        }
    }


    public long timeOfDay() {
        return System.nanoTime() / 1000;

    }

    private void checkClient() throws FixedAnnounceException {
        String method = request.getMethod();
        if (!method.equals("GET")) {
            throw new InvalidAnnounceException("Invalid request method: " + method);
        }
        if(request.getHeader("User-Agent") == null){
            throw new InvalidAnnounceException("Bad client: User-Agent cannot be empty");
        }
        //blacklistClient.checkClient(request);
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

    @NotNull
    private String generatePeersResponse(String peerIdHash, String infoHash, int numWant, boolean compact) {
        if (compact) {
            return generatePeersResponseCompat(peerIdHash, infoHash, numWant);
        } else {
            return generatePeersResponseNonCompat(peerIdHash, infoHash, numWant, compact);
        }
    }

    @NotNull
    private String generatePeersResponseCompat(String peerId, String infoHash, int numWant) {
        // TODO: What the fuck
        // http://bittorrent.org/beps/bep_0023.html
        PeerResult peers = gatherPeers(peerId, infoHash, numWant);
        Map<String, Object> dict = new HashMap<>();
        dict.put("interval", randomInterval());
        dict.put("min interval", randomInterval());
        dict.put("peers", peers.peers());
        dict.put("peers6", peers.peers6());
        return new String(BITTORRENT_STANDARD_BENCODE_ENCODER.encode(dict), BITTORRENT_STANDARD_BENCODE_ENCODER.getCharset());
    }

    @NotNull
    private String generatePeersResponseNonCompat(String peerId, String infoHash, int numWant, boolean noPeerId) {
        PeerResult peers = gatherPeers(peerId, infoHash, numWant);
        List<Map<String, String>> peerList = new ArrayList<>();
        List<Peer> allPeers = new ArrayList<>(peers.peers());
        allPeers.addAll(peers.peers6());
        for (Peer peer : allPeers) {
            Map<String, String> peerMap = new HashMap<>();
            if (!noPeerId) {
                peerMap.put("peer id", peer.getPeerId());
            }
            peerMap.put("ip", peer.getIp());
            peerMap.put("port", String.valueOf(peer.getPort()));
            peerList.add(peerMap);
        }
        Map<String, Object> dict = new HashMap<>();
        dict.put("interval", randomInterval());
        dict.put("peers", peerList);
        return new String(BITTORRENT_STANDARD_BENCODE_ENCODER.encode(dict), BITTORRENT_STANDARD_BENCODE_ENCODER.getCharset());
    }

    @NotNull
    private PeerResult gatherPeers(@NotNull String peerId, @NotNull String infoHash, int numWant) {
        List<Peer> torrentPeers = peerService.fetchPeers(infoHash, numWant);
        // Remove itself
        torrentPeers.removeIf(peer -> peer.getPeerId().equals(peerId));
        List<Peer> v4 = torrentPeers.stream().filter(peer -> ipValidator.isValidInet4Address(peer.getIp())).toList();
        List<Peer> v6 = torrentPeers.stream().filter(peer -> ipValidator.isValidInet6Address(peer.getIp())).toList();
        long completed = torrentPeers.stream().filter(Peer::isSeeder).count();
        long incompleted = torrentPeers.size() - completed;
        return new PeerResult(v4, v6, completed, incompleted);
    }

    private String randomInterval() {
        return String.valueOf(random.nextInt(MIN_INTERVAL, MAX_INTERVAL));
    }

    record PeerResult(@NotNull List<Peer> peers, List<Peer> peers6, long complete, long incomplete) {
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
