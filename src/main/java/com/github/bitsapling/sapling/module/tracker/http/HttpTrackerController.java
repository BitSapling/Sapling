package com.github.bitsapling.sapling.module.tracker.http;

import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.module.setting.SettingService;
import com.github.bitsapling.sapling.module.torrent.Torrent;
import com.github.bitsapling.sapling.module.torrent.TorrentMetadata;
import com.github.bitsapling.sapling.module.torrent.TorrentMetadataService;
import com.github.bitsapling.sapling.module.torrent.TorrentService;
import com.github.bitsapling.sapling.module.tracker.Peer;
import com.github.bitsapling.sapling.module.tracker.PeerService;
import com.github.bitsapling.sapling.module.tracker.exception.FixedAnnounceException;
import com.github.bitsapling.sapling.module.tracker.exception.InvalidAnnounceException;
import com.github.bitsapling.sapling.module.tracker.exception.RetryableAnnounceException;
import com.github.bitsapling.sapling.module.tracker.handle.AnnounceQueueService;
import com.github.bitsapling.sapling.module.tracker.handle.AnnounceTask;
import com.github.bitsapling.sapling.module.uacontrol.BtClientUAService;
import com.github.bitsapling.sapling.module.user.User;
import com.github.bitsapling.sapling.module.user.UserService;
import com.github.bitsapling.sapling.util.BencodeUtil;
import com.github.bitsapling.sapling.util.IPUtil;
import com.github.bitsapling.sapling.util.SafeUUID;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Slf4j
public class HttpTrackerController {
    private static final URLCodec codec = new URLCodec();
    @Autowired
    private Random random;
    @Autowired
    private BtClientUAService clientUaControl;
    @Autowired
    private UserService userService;
    @Autowired
    private TorrentService torrentService;
    @Autowired
    private AnnounceQueueService queueService;
    @Autowired
    private PeerService peerService;
    @Autowired
    private TorrentMetadataService torrentMetadataService;
    @Autowired
    private SettingService setting;


    @GetMapping("/scrape")
    public ResponseEntity<String> scrape(@RequestParam Map<String, String> gets, HttpServletRequest request) throws FixedAnnounceException, RetryableAnnounceException {
        // https://wiki.vuze.com/w/Scrape
        String passkey = gets.get("passkey");
        if (StringUtils.isEmpty(passkey)) {
            throw new InvalidAnnounceException("You must re-download the torrent from tracker for seeding.");
        }
        if (!SafeUUID.isUUID(passkey)) {
            throw new InvalidAnnounceException("Invalid passkey: This passkey not registered in this tracker.");
        }
        String userAgent = request.getHeader("User-Agent");
        String peerId = gets.get("peer_id");
        if (userAgent == null && peerId == null) {
            throw new InvalidAnnounceException("Invalid announce: Both user-agent and peer-id is null.");
        }
        checkClient(userAgent, peerId);
        User user = userService.getUserByPasskey(passkey);
        checkUser(user, passkey, "scrape");
        Map<String, Object> dict = new LinkedHashMap<>();
        Map<String, Integer> flags = new LinkedHashMap<>();
        flags.put("min_request_interval", randomInterval());
        dict.put("flags", flags);
        Map<String, Map<String, Object>> files = new LinkedHashMap<>();
        for (String infoHash : request.getParameterValues("info_hash")) {
            try {
                infoHash = codec.decode(infoHash);
            } catch (DecoderException e) {
                log.debug("Ignoring {}, failed to decode it", infoHash, e);
            }
            Torrent torrent = torrentService.getTorrentByInfoHash(infoHash);
            if (torrent == null) {
                continue;
            }
            Map<String, Object> meta = new LinkedHashMap<>();
            TorrentMetadata metadata = torrentMetadataService.getTorrentMetadataByTorrentId(torrent.getId());
            if (metadata == null)
                continue;
            meta.put("downloaded", metadata.getTimesCompleted());
            meta.put("complete", metadata.getSeeders());
            meta.put("incomplete", metadata.getLeechers());
            meta.put("downloaders", metadata.getTimesFileDownloaded());
            files.put(infoHash, meta);
        }
        dict.put("files", files);
        String resp = BencodeUtil.convertToString(BencodeUtil.bittorrent().encode(dict));
        log.debug("Scrape processed with scraped {} torrents returns.", files.size());
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain; charset=iso-8859-1")
                .body(resp);
    }

    @GetMapping("/announce")
    public ResponseEntity<String> announce(@RequestParam Map<String, String> queryStrings, HttpServletRequest request) throws RetryableAnnounceException, InvalidAnnounceException {
        AnnounceData announceData = parseRequest(request, queryStrings);
        int maxAnnouncePeersReturned = setting.getSetting("tracker.max_peers_announce_returns").getValueAsInteger(300);
        announceData.setNumWant(Math.min(announceData.getNumWant(), maxAnnouncePeersReturned));
        checkClient(announceData.getUserAgent(), announceData.getPeerId());
        User user = userService.getUserByPasskey(announceData.getPasskey());
        checkUser(user, announceData.getPasskey(), "announce");
        Torrent torrent = torrentService.getTorrentByInfoHash(announceData.getInfoHash());
        checkTorrent(torrent, announceData);
        Long length = queueService.insertTask(new AnnounceTask(announceData, user.getId(), torrent.getId()));
        PeerResult result = feedPeers(announceData, torrent);
        long peersCount = result.peers().size() + result.peers6().size();
        String body = BencodeUtil.convertToString(BencodeUtil.bittorrent().encode(generatePeersResponse(result, announceData.isCompact(), announceData.isNoPeerId())));
        log.info("Announce processed with {} peers given, from user {} and ip {} for torrent {}. ({} tasks remains in queue)", peersCount, user.getUsername(), announceData.getClientRequestIp(), torrent.getId(), length);
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain; charset=iso-8859-1")
                .body(body);
    }


    private void checkTorrent(Torrent torrent, AnnounceData announceData) throws InvalidAnnounceException, RetryableAnnounceException {
        if (torrent == null) {
            throw new InvalidAnnounceException("Invalid info hash: Torrent " + announceData.getInfoHash() + " are not registered on this tracker.");
        }
        if (torrent.getIsDraft()) {
            throw new RetryableAnnounceException("Draft torrent: Torrent " + announceData.getInfoHash() + " are not published to public on this tracker yet.", 60 * 60 * 8);
        }
        if (torrent.getIsReview()) {
            throw new RetryableAnnounceException("Torrent reviewing: Torrent " + announceData.getInfoHash() + " are under review and waiting a moderator to approve it.", 60 * 60 * 8);
        }
        if (torrent.getIsBanned()) {
            throw new InvalidAnnounceException("Banned torrent: Torrent " + announceData.getInfoHash() + " are banned on this tracker.");
        }
    }

    private void checkUser(User user, String passkey, String checkType) throws InvalidAnnounceException, RetryableAnnounceException {
        if (user == null) {
            throw new InvalidAnnounceException("Invalid passkey: " + passkey + " are not registered on this tracker.");
        }
        if (user.getIsBanned()) {
            throw new RetryableAnnounceException("Banned user: " + user.getUsername() + " are banned on this tracker.", 60 * 60 * 24);
        }
        switch (checkType) {
            case "announce" -> {
                if (!StpUtil.hasPermission(user.getId(), "torrent:announce")) {
                    throw new RetryableAnnounceException("Permission Denied: " + user.getUsername() + " are not allowed to announce torrents.", 60 * 60 * 8);
                }
            }
            case "scrape" -> {
                if (!StpUtil.hasPermission(user.getId(), "torrent:scrape")) {
                    throw new RetryableAnnounceException("Permission Denied: " + user.getUsername() + " are not allowed to scrape torrents.", 60 * 60 * 8);
                }
            }
            default -> {
            } // do nothing
        }

    }

    private PeerResult feedPeers(AnnounceData data, Torrent torrent) {
        TorrentMetadata metadata = torrentMetadataService.getTorrentMetadata(torrent.getId());
        if (metadata == null) {
            metadata = new TorrentMetadata(-1L, torrent.getId(), 0L, 0L, 0L, 0L);
        }
        List<Peer> fetchedPeers = peerService.getPeersByTorrentRandom(torrent.getId(), data.getNumWant());
        List<Peer> ipv4 = new ArrayList<>();
        List<Peer> ipv6 = new ArrayList<>();
        for (Peer fetchedPeer : fetchedPeers) {
            if (IPUtil.isIPv4(fetchedPeer.getIp())) {
                ipv4.add(fetchedPeer);
                continue;
            }
            if (IPUtil.isIPv6(fetchedPeer.getIp())) {
                ipv6.add(fetchedPeer);
            }
        }

        return new PeerResult(ipv4, ipv6, metadata.getSeeders(), metadata.getLeechers(), metadata.getTimesCompleted(), metadata.getTimesFileDownloaded());

    }


    private void checkClient(String userAgent, String peerId) throws InvalidAnnounceException {
        boolean uaCheck = clientUaControl.isAllowedClient(userAgent);
        boolean peerIdCheck = clientUaControl.isAllowedClient(peerId);
        if (!uaCheck && !peerIdCheck) {
            throw new InvalidAnnounceException("Disallowed client: " + userAgent + "(" + peerId + ") are not allowed to use on this tracker.");
        }
    }

    private AnnounceData parseRequest(HttpServletRequest request, Map<String, String> queryStrings) throws RetryableAnnounceException {
        try {
            HttpQueryTrackerParser parser = new HttpQueryTrackerParser(request, queryStrings);
            return parser.toAnnounceData();
        } catch (DecoderException e) {
            throw new RetryableAnnounceException("Failed to decode query string: " + e.getMessage(), 60);
        }
    }

    @NotNull
    private Map<String, Object> generatePeersResponse(PeerResult result, boolean compact, boolean noPeerId) {
        Map<String, Object> resp;
        if (compact) {
            resp = generatePeersResponseCompat(result);
        } else {
            resp = generatePeersResponseNonCompat(result, noPeerId);
        }
        return resp;
    }

    @NotNull
    private Map<String, Object> generatePeersResponseCompat(@NotNull PeerResult result) {
        Map<String, Object> dict = new HashMap<>();
        dict.put("interval", randomInterval());
        dict.put("complete", result.complete());
        dict.put("incomplete", result.incomplete());
        dict.put("downloaded", result.downloaders());
        dict.put("downloaders", result.downloaders());
        dict.put("peers", BencodeUtil.compactPeers(result.peers().stream().map(p -> new BencodeUtil.BencodePeer(p.getIp(), p.getPort())).toList(), false));
        if (result.peers6().size() > 0) {
            dict.put("peers6", BencodeUtil.compactPeers(result.peers6().stream().map(p -> new BencodeUtil.BencodePeer(p.getIp(), p.getPort())).toList(), true));
        }
        return dict;
    }

    @NotNull
    private Map<String, Object> generatePeersResponseNonCompat(@NotNull PeerResult result, boolean noPeerId) {
        List<Map<String, Object>> peerList = new ArrayList<>();
        List<Peer> allPeers = new ArrayList<>();
        allPeers.addAll(result.peers());
        allPeers.addAll(result.peers6());
        for (Peer peer : allPeers) {
            Map<String, Object> peerMap = new LinkedHashMap<>();
            if (!noPeerId) {
                peerMap.put("peer id", peer.getPeerId());
            }
            peerMap.put("ip", peer.getIp());
            peerMap.put("port", peer.getPort());
            peerList.add(peerMap);
        }
        Map<String, Object> dict = new HashMap<>();
        dict.put("interval", randomInterval());
        dict.put("complete", result.complete());
        dict.put("incomplete", result.incomplete());
        dict.put("downloaded", result.downloaded());
        dict.put("downloaders", result.downloaders());
        dict.put("peers", peerList);
        return dict;
    }

    private int randomInterval() {
        int min = Integer.parseInt(setting.getSetting("tracker.min_announce_interval").getValue());
        int max = Integer.parseInt(setting.getSetting("tracker.max_announce_interval").getValue());
        return random.nextInt(min, max);
    }

    record PeerResult(@NotNull List<Peer> peers, List<Peer> peers6, long complete, long incomplete, long downloaded,
                      long downloaders) {
    }


}
