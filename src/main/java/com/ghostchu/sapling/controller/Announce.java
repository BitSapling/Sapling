package com.ghostchu.sapling.controller;

import com.dampcake.bencode.Bencode;
import com.ghostchu.sapling.domain.entity.Peer;
import com.ghostchu.sapling.domain.entity.Torrent;
import com.ghostchu.sapling.domain.entity.User;
import com.ghostchu.sapling.domain.type.Promotion;
import com.ghostchu.sapling.exception.*;
import com.ghostchu.sapling.language.ITranslation;
import com.ghostchu.sapling.service.*;
import com.ghostchu.sapling.util.ConstMetadata;
import com.ghostchu.sapling.util.IpUtil;
import com.ghostchu.sapling.util.SafeUUID;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * BitTorrent Announce
 */
@RestController
@RequestMapping("/announce")
public class Announce {
    private static final int MAX_PEERS = 50;
    private static final int MAX_TORRENT_SEEDING_PEERS = 3 * 2; // 3 locations for double stack
    private static final int ANNOUNCE_INTERVAL_SECONDS = 120;
    private static final int ANNOUNCE_MIN_INTERVAL_SECONDS = 30;
    private final HttpServletRequest request;
    private final AnnounceService service;
    private final UserService userService;
    private final PunishmentService punishmentService;
    private final TorrentService torrentService;
    private final PromotionService promotionService;
    private final SeedBoxService seedBoxService;
    private final ITranslation lang;
    private final Bencode bencode = new Bencode();
    private final InetAddressValidator ipValidator = InetAddressValidator.getInstance();
    private final Logger LOG = LoggerFactory.getLogger(Announce.class);


    public Announce(@Autowired HttpServletRequest request, @Autowired AnnounceService service, @Autowired TorrentService torrentService, @Autowired UserService userService, @Autowired PunishmentService punishmentService, @Autowired PromotionService promotionService, @Autowired SeedBoxService seedBoxService, @Autowired ITranslation lang) {
        this.request = request;
        this.service = service;
        this.torrentService = torrentService;
        this.userService = userService;
        this.punishmentService = punishmentService;
        this.promotionService = promotionService;
        this.seedBoxService = seedBoxService;
        this.lang = lang;
    }

    @NotNull
    private String getUserAgent() {
        String ua = request.getHeader("User-Agent");
        if (ua == null) {
            ua = "Unknown";
        }
        return ua;
    }

    @GetMapping("/")
    public String announce(@RequestParam("passkey") String passKeyStr, @RequestParam("info_hash") String infoHash,
                           @RequestParam("peer_id") String peerId, @RequestParam("peer_id") String event,
                           @RequestParam("port") Integer port, @RequestParam("downloaded") Integer downloaded,
                           @RequestParam("uploaded") Integer uploaded, @RequestParam("left") Integer left,
                           @RequestParam("compact") Integer compact, @RequestParam("no_peer_id") Integer noPeerId,
                           @Nullable @RequestParam("ipv4") String ipv4, @Nullable @RequestParam("ipv6") String ipv6,
                           @Nullable @RequestParam("numwant") Integer numberOfPeers1,
                           @Nullable @RequestParam("num want") Integer numberOfPeers2,
                           @Nullable @RequestParam("num_want") Integer numberOfPeers3) {
        try {
            checkUserAgent();
            @NotNull String ip = IpUtil.getIpAddr(request);
            if (!ipValidator.isValid(ip))
                return error(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.invalid_ip", ip));
            if (ipv4 == null && ipValidator.isValidInet4Address(ip)) ipv4 = ip;
            if (ipv6 == null && ipValidator.isValidInet6Address(ip)) ipv6 = ip;
            if (punishmentService.isBanned(ip, ipv4, ipv6))
                throw new PeerBannedException(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.banned", ip + " or " + ipv4 + " or " + ipv6));
            // Check necessary params
            Validate.notNull(passKeyStr, lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.missing_key", "passkey"));
            UUID passkey = SafeUUID.fromString(passKeyStr);
            Validate.notNull(passkey, lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.missing_key", "passkey"));
            Validate.notNull(infoHash, lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.missing_key", "info_hash"));
            Validate.notNull(peerId, lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.missing_key", "peer_id"));
            Validate.notNull(port, lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.missing_key", "port"));
            Validate.notNull(downloaded, lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.missing_key", "downloaded"));
            Validate.notNull(uploaded, lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.missing_key", "uploaded"));
            Validate.notNull(left, lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.missing_key", "left"));
            // Validate syntax of params
            Validate.isTrue(infoHash.length() == 20, lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.invalid_key", "info_hash"));
            Validate.isTrue(peerId.length() == 20, lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.invalid_key", "peer_id"));
            Validate.exclusiveBetween(Short.MAX_VALUE, Short.MIN_VALUE, port, lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.invalid_key", "port"));
            int wantedPeers = MAX_PEERS;
            if (numberOfPeers1 != null) wantedPeers = numberOfPeers1;
            if (numberOfPeers2 != null) wantedPeers = numberOfPeers2;
            if (numberOfPeers3 != null) wantedPeers = numberOfPeers3;
            wantedPeers = Math.min(wantedPeers, MAX_PEERS);
            boolean useCompatResponse = compact != null && compact == 1;
            return processAnnounce(passkey, infoHash, peerId, event, port, downloaded, uploaded, left, useCompatResponse, noPeerId, ipv4, ipv6, wantedPeers);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    private void checkUserAgent() throws NotAllowedClientException {
        if (!service.isBitTorrentClient(getUserAgent())) {
            throw new NotAllowedClientException(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.browser_access_denied"));
        }
        if (!service.isAllowedClient(getUserAgent())) {
            throw new NotAllowedClientException(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.not_allowed_client", request.getHeader("User-Agent")));
        }
    }

    private void checkUser(@Nullable User user) throws AnnounceException {
        if (user == null)
            throw new PasskeyInvalidException(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.passkey_invalid"));
        if (user.isParked())
            throw new AccountSuspendException(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.account_parked"));
        if (service.isUserDownloadPrivilegeDisabled(user))
            throw new AccountSuspendException(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.account_download_privileges_disabled"));
    }

    private void checkTorrent(@Nullable Torrent torrent, @NotNull String infoHash, @NotNull User user) throws AnnounceException {
        if (torrent == null)
            throw new TorrentNotExistException(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.torrent_not_exists", infoHash));
    }

    private void checkTorrentAmounts(@NotNull Torrent torrent, @NotNull User user, Peer peer, boolean seeder) throws TooManyPeersException {
        if (seeder) {
            Set<Peer> seedingPeers = new HashSet<>(service.fetchUserTorrentSeedingPeers(torrent, user));
            seedingPeers.add(peer);
            if (seedingPeers.size() > MAX_TORRENT_SEEDING_PEERS)
                throw new TooManyPeersException(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.too_many_seeding", MAX_TORRENT_SEEDING_PEERS));
        } else {
            // leech
            if (!service.fetchUserTorrentSeedingPeers(torrent, user).isEmpty())
                throw new TooManyPeersException(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.already_downloading", 1));
        }
    }

    private void checkAnnounceCooldown(@NotNull Torrent torrent, Peer peer) throws AnnounceCooldownException {
        if (service.isCooldownHit(torrent, peer))
            throw new AnnounceCooldownException(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.announce_cooldown"));
        service.markCooldown(torrent, peer);

    }

    private String processAnnounce(@NotNull UUID passkey, @NotNull String infoHash,
                                   @NotNull String peerId, @Nullable String event,
                                   int port, int downloaded,
                                   int uploaded, int left,
                                   boolean compact, Integer noPeerId,
                                   @Nullable String ipv4, @Nullable String ipv6,
                                   int peersWanted) throws AnnounceException {
        if (service.isPortBanned(port))
            throw new PeerBannedException(lang.parse(ConstMetadata.DEFAULT_LOCALE, "announce.banned_port", port));
        // Determine the IP address
        User user = userService.getUser(passkey);
        checkUser(user);
        Torrent torrent = torrentService.getTorrent(infoHash);
        checkTorrent(torrent, infoHash, user);
        boolean seeder = left == 0;
        Peer userPeer = service.getPeer(user, torrent, peerId);
        if (userPeer == null) {
            LOG.debug("Detected new Peer connected to tracker, creating a new Peer instance: {} {} {} {} {} {} {} {} {}", user.getUserId(), peerId, getUserAgent(), ipv4, ipv6, port, seeder, new Date(), left);
            userPeer = new Peer(user.getUserId(), peerId, getUserAgent(), ipv4, ipv6, port, seeder, new Date(), left);
        } else {
            // Update user peer information
            userPeer.setSeeder(seeder);
            userPeer.setClientSlug(getUserAgent());
            userPeer.setPrevActivity(userPeer.getLastActivity());
            userPeer.setLastActivity(new Date());
            userPeer.setLeft(left);
            if (ipv4 != null)
                userPeer.setIpv4(ipv4);
            if (ipv6 != null)
                userPeer.setIpv6(ipv6);
            userPeer.setPort(port);
        }
        checkAnnounceCooldown(torrent, userPeer);
        checkTorrentAmounts(torrent, user, userPeer, seeder);
        boolean dropPeerId = noPeerId == 1;
        return handleAnnounce(torrent, event, downloaded, uploaded, seeder, compact, userPeer, peersWanted, dropPeerId);
    }

    @NotNull
    public String handleAnnounce(@NotNull Torrent torrent, @Nullable String event,
                                 int actualDownloaded, int actualUploaded,
                                 boolean seeder, boolean compact, Peer userPeer,
                                 int peersWanted, boolean dropPeerId) {
        Promotion seedPromotion = promotionService.queryPromotion(torrent);
        if (seedBoxService.isSeedBoxSkipPromotion()) {
            if (seedBoxService.isSeedBox(userPeer.getIpv4()) || seedBoxService.isSeedBox(userPeer.getIpv6())) {
                LOG.debug("Peer {} is a seed-box, remove the promotion.", userPeer.getPeerId());
                seedPromotion = null;
            }
        }
        int downloaded = actualDownloaded;
        int uploaded = actualUploaded;
        if (seedPromotion != null) {
            downloaded *= seedPromotion.getDownloadOff();
            uploaded *= seedPromotion.getUploadOff();
        }

        long thisTimeActualUploaded = Math.max(0, actualUploaded - userPeer.getActualUploaded());
        long thisTimeActualDownloaded = Math.max(0, actualDownloaded - userPeer.getActualDownloaded());
        long thisTimeUploaded = Math.max(0, uploaded - userPeer.getUploaded());
        long thisTimeDownloaded = Math.max(0, downloaded - userPeer.getDownloaded());
        userPeer.setActualDownloaded(actualDownloaded);
        userPeer.setActualUploaded(actualUploaded);
        long announcePeriodSeconds = new Date().toInstant().getEpochSecond() - userPeer.getLastActivity().toInstant().getEpochSecond();
        LOG.debug("Peer {}: Uploaded: {}, Downloaded: {}, ActualUploaded: {}, ActualDownloaded: {} in {} seconds.", userPeer.getPeerId(), uploaded, downloaded, actualUploaded, actualDownloaded, announcePeriodSeconds);
        LOG.debug("Peer {}: (since last announce) Uploaded: {}, Downloaded: {}, ActualUploaded: {}, ActualDownloaded: {} in {} seconds.", userPeer.getPeerId(), thisTimeUploaded, thisTimeDownloaded, thisTimeActualUploaded, thisTimeActualDownloaded, announcePeriodSeconds);
        checkOverSpeed(userPeer, announcePeriodSeconds, thisTimeActualDownloaded, thisTimeActualUploaded);

        if (event != null) {
            switch (event) {
                case "started", "start" -> service.start(userPeer, torrent);
                case "completed", "complete" -> service.completed(userPeer, torrent);
                case "stopped", "stop" ->// Stopped
                        service.stopped(userPeer, torrent);
                default -> LOG.debug("Undefined event {}", event);
            }
        }
        // Grab Peers
        return bakePeers(userPeer, torrent, seeder, compact, peersWanted, dropPeerId);
    }

    @NotNull
    private String bakePeers(@NotNull Peer userPeer, @NotNull Torrent torrent, boolean seeder, boolean compact, int peersWanted, boolean dropPeerId) {
        Collection<Peer> peers = service.fetchPeers(userPeer, torrent, seeder, peersWanted);
        Map<String, Object> resp = new HashMap<>();
        resp.put("interval", ANNOUNCE_INTERVAL_SECONDS);
        resp.put("min interval", ANNOUNCE_MIN_INTERVAL_SECONDS);
        resp.put("complete", peers.stream().filter(Peer::isSeeder).count());
        resp.put("incomplete", peers.stream().filter(peer -> !peer.isSeeder()).count());
        resp.put("leechers", peers.stream().filter(peer -> peer.getActualDownloaded() > peer.getActualUploaded()).count());
        resp.put("peers", bakePeersNoCompat(peers, dropPeerId));
        // TODO compat format
        String response = new String(bencode.encode(resp), bencode.getCharset());
        LOG.debug("Reply the peer {} with data {}", userPeer.getPeerId(), response);
        return response;
    }

    //    private byte[] bakePeersCompat(@NotNull Collection<Peer> peers, boolean dropPeerId){
//        List<Map<String, Object>> mapping = new LinkedList<>();
//        for (Peer peer : peers) {
//            if(peer.getIpv4() != null) {
//                Map<String, Object> peerResponse = new HashMap<>();
//                peerResponse.put("ip", peer.getIpv4());
//                peerResponse.put("port", peer.getPort());
//                mapping.add(peerResponse);
//            }
//
//            if(peer.getIpv6() != null) {
//                Map<String, Object> peerResponse = new HashMap<>();
//                peerResponse.put("ip", peer.getIpv6());
//                peerResponse.put("port", peer.getPort());
//                mapping.add(peerResponse);
//            }
//        }
//        return PackUtil.pack(bencode.encode(mapping));
//    }
    private Object bakePeersNoCompat(@NotNull Collection<Peer> peers, boolean dropPeerId) {
        List<Map<String, Object>> mapping = new LinkedList<>();
        for (Peer peer : peers) {
            if (peer.getIpv4() != null) {
                Map<String, Object> peerResponse = new HashMap<>();
                if (!dropPeerId)
                    peerResponse.put("peer id", peer.getPeerId());
                peerResponse.put("ip", peer.getIpv4());
                peerResponse.put("port", peer.getPort());
                mapping.add(peerResponse);
            }

            if (peer.getIpv6() != null) {
                Map<String, Object> peerResponse = new HashMap<>();
                if (!dropPeerId)
                    peerResponse.put("peer id", peer.getPeerId());
                peerResponse.put("ip", peer.getIpv6());
                peerResponse.put("port", peer.getPort());
                mapping.add(peerResponse);
            }
        }
        return mapping;
    }

    private void checkOverSpeed(@NotNull Peer userPeer, long announcePeriodSeconds, long thisTimeActualDownloaded, long thisTimeActualUploaded) {
        if (service.isOverSpeed(announcePeriodSeconds, thisTimeActualUploaded)) {
            // Suspend the account

        }
    }

    @NotNull
    private String error(@NotNull String reason) {
        return new String(bencode.encode(Map.of("failure reason", reason)), bencode.getCharset());
    }

}
