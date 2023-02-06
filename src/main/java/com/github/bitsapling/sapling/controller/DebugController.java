package com.github.bitsapling.sapling.controller;

import com.github.bitsapling.sapling.entity.PeerEntity;
import com.github.bitsapling.sapling.entity.TorrentEntity;
import com.github.bitsapling.sapling.exception.TorrentException;
import com.github.bitsapling.sapling.objects.*;
import com.github.bitsapling.sapling.repository.PeersRepository;
import com.github.bitsapling.sapling.repository.TorrentRepository;
import com.github.bitsapling.sapling.service.*;
import com.github.bitsapling.sapling.util.TorrentParser;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@RestController
@Slf4j
public class DebugController {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private UserService userService;
    @Autowired
    private TorrentService torrentService;
    @Autowired
    private PeerService peerService;
    @Autowired
    private PeersRepository peersRepository;
    @Autowired
    private TorrentRepository torrentRepository;
    @Autowired
    private AnnouncePerformanceMonitorService announcePerformanceMonitorService;
    @Autowired
    private AnnounceService announceService;

    private String page;

    {
        try {
            page = Files.readString(new File("landing-debug.html").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/")
    public String torrents() throws IOException {
        log.debug("Accepted");
        long startTime = System.currentTimeMillis();
        StringJoiner peersJoiner = new StringJoiner("\n\n");
        StringJoiner torrentsJoiner = new StringJoiner("\n\n");
        List<Torrent> torrents = new ArrayList<>();
        List<Peer> peers = new ArrayList<>();
        log.debug("Accepting torrents");
        long dbTimeStart = System.currentTimeMillis();
        for (TorrentEntity entity : torrentRepository.findAll()) {
            Torrent torrent = torrentService.convert(entity);
            torrents.add(torrent);
            torrentsJoiner.add(torrent.toString());
        }
        log.debug("Accepting peers");
        for (PeerEntity entity : peersRepository.findAll()) {
            Peer peer = peerService.convert(entity);
            Peer copiedPeer = new Peer(peer.getId(), peer.getIp(), peer.getPort(), peer.getInfoHash(), "<peerid>",
                    peer.getUserAgent(), "<passkey-removed>", peer.getUploaded(),
                    peer.getDownloaded(), peer.getLeft(), peer.isSeeder(), peer.getUpdateAt(),
                    peer.getSeedingTime());
            peersJoiner.add(copiedPeer.toString());
            peers.add(peer);
        }
        long dbTimeEnd = System.currentTimeMillis() - dbTimeStart;
        log.debug("Replacing");
        String resp = page.replace("%%torrents_amount%%", String.valueOf(torrents.size()));
        resp = resp.replace("%%peers_amount%%", String.valueOf(peers.size()));
        resp = resp.replace("%%announce_reqs%%", String.valueOf(announcePerformanceMonitorService.getAnnounceTimes().size()));
        resp = resp.replace("%%announce_ms%%", String.valueOf(announcePerformanceMonitorService.avgMs()));
        resp = resp.replace("%%startup_date%%", announcePerformanceMonitorService.getStartTime().toString());
        resp = resp.replace("%%announce_count%%", String.valueOf(announcePerformanceMonitorService.getHandled()));
        resp = resp.replace("%%announce_jobs%%", String.valueOf(announceService.getTaskQueue().size()));
        resp = resp.replace("%%peers_list%%", peersJoiner.toString());
        resp = resp.replace("%%torrents_list%%", torrentsJoiner.toString());
        resp = resp.replace("%%debug_page_db_consumed%%", String.valueOf(dbTimeEnd));
        resp = resp.replace("%%debug_page_consumed%%", String.valueOf(System.currentTimeMillis() - startTime));
        resp = resp.replace("%%announce_job_avg%%", String.valueOf(announcePerformanceMonitorService.avgJobMs()));

        log.debug("Done!");
        return resp;
    }

    @GetMapping("/debug/parseTorrents")
    public String parseTorrent() throws IOException, TorrentException {
        StringJoiner joiner = new StringJoiner("\n");
        TorrentParser parser = new TorrentParser(new File("1.torrent"));
        joiner.add("File Size: " + parser.getTorrentFilesSize());
        joiner.add("Pieces Length: " + parser.getTorrentFilesSize());
        Map<String, Long> fileList = parser.getFileList();
        fileList.forEach((key, value) -> joiner.add("File -> " + key + ", Size -> " + value));
        return joiner.toString();
    }

    @GetMapping("/debug/initTables")
    @Transactional
    public String initTables() {
        try {
            List<Permission> permissions = new ArrayList<>();
            permissions.add(new Permission(0, "torrent:announce", true));
            permissions.add(new Permission(0, "torrent:upload", true));
            permissions.add(new Permission(0, "torrent:scrape", true));
            permissions.forEach(p -> permissionService.save(p));
            PromotionPolicy promotionPolicy = new PromotionPolicy(0, "系统默认", 1.0d, 1.0d);
            promotionService.save(promotionPolicy);
            UserGroup userGroup = new UserGroup(0, "default", "Lv.1 青铜", permissions, promotionPolicy);
            userGroupService.save(userGroup);
            User user = new User(0,
                    "test@test.com",
                    "$2a$06$r6QixzXG/Y8mUtmCV7b70.Jp7qjOL2nONUJolzGmQPzVn2acoKLf6",
                    "TestUser1",
                    userGroup,
                    new UUID(0, 0).toString(),
                    Timestamp.from(Instant.now()),
                    "https://www.baidu.com/favicon.ico",
                    "这是自定义头衔",
                    "这是测试签名",
                    "zh-CN",
                    "1000mbps",
                    "1000mbps",
                    0,
                    0,
                    0,
                    0,
                    "中国移不动",
                    BigDecimal.ZERO,
                    0,
                    0);
            userService.save(user);
            log.info("创建测试用户 1 成功");
            User user2 = new User(0,
                    "test2@test.com",
                    "$2a$06$r6QixzXG/Y8mUtmCV7b70.Jp7qjOL2nONUJolzGmQPzVn2acoKLf6",
                    "TestUser2",
                    userGroup,
                    new UUID(0, 0).toString(),
                    Timestamp.from(Instant.now()),
                    "https://weibo.com/favicon.ico",
                    "这是自定义头衔2",
                    "这是测试签名2",
                    "en-US",
                    "10000mbps",
                    "10000mbps",
                    0,
                    0,
                    0,
                    0,
                    "中国联不通",
                    BigDecimal.ZERO,
                    5,
                    0);
            userService.save(user2);
            log.info("创建测试用户 2 成功");
            return "初始化基本数据库测试内容成功";
        } catch (Exception e) {
            log.error("Error: ", e);
            throw e;
        }
    }

    // A function that censor half of original string to *
    public String censor(String original) {
        StringBuilder result = new StringBuilder();
        int length = original.length();
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                result.append(original.charAt(i));
            } else {
                result.append("*");
            }
        }
        return result.toString();
    }
}
