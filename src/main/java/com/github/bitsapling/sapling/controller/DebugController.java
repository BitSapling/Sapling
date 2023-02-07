package com.github.bitsapling.sapling.controller;

import com.github.bitsapling.sapling.entity.Category;
import com.github.bitsapling.sapling.entity.Peer;
import com.github.bitsapling.sapling.entity.Permission;
import com.github.bitsapling.sapling.entity.PromotionPolicy;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.entity.UserGroup;
import com.github.bitsapling.sapling.exception.TorrentException;
import com.github.bitsapling.sapling.repository.CategoryRepository;
import com.github.bitsapling.sapling.repository.PeersRepository;
import com.github.bitsapling.sapling.repository.TorrentRepository;
import com.github.bitsapling.sapling.service.AnnouncePerformanceMonitorService;
import com.github.bitsapling.sapling.service.CategoryService;
import com.github.bitsapling.sapling.service.PermissionService;
import com.github.bitsapling.sapling.service.PromotionService;
import com.github.bitsapling.sapling.service.UserGroupService;
import com.github.bitsapling.sapling.service.UserService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

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
    private PeersRepository peersRepository;
    @Autowired
    private TorrentRepository torrentRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AnnouncePerformanceMonitorService announcePerformanceMonitorService;

    @GetMapping("/")
    public String torrents() throws IOException {
        String page = Files.readString(new File("landing-debug.html").toPath());
        long startTime = System.currentTimeMillis();
        StringJoiner peersJoiner = new StringJoiner("\n\n");
        StringJoiner torrentsJoiner = new StringJoiner("\n\n");
        List<Torrent> torrents = new ArrayList<>();
        List<Peer> peers = new ArrayList<>();
        long dbTimeStart = System.currentTimeMillis();
        for (Torrent entity : torrentRepository.findAll()) {
            torrents.add(entity);
            torrentsJoiner.add(entity.toString());
        }
        for (Peer peer : peersRepository.findAll()) {
            Peer copiedPeer = new Peer(
                    peer.getId(), peer.getIp(), peer.getPort(), peer.getInfoHash(), "<peerid>",
                    peer.getUserAgent(), peer.getUploaded(),
                    peer.getDownloaded(), peer.getLeft(), peer.isSeeder(), "<passkey-removed>",
                    peer.getUpdateAt(),
                    peer.getSeedingTime());
            peersJoiner.add(copiedPeer.toString());
            peers.add(peer);
        }
        long dbTimeEnd = System.currentTimeMillis() - dbTimeStart;
        String resp = page.replace("%%torrents_amount%%", String.valueOf(torrents.size()));
        resp = resp.replace("%%peers_amount%%", String.valueOf(peers.size()));
        resp = resp.replace("%%announce_reqs%%", String.valueOf(announcePerformanceMonitorService.getAnnounceTimes().size()));
        resp = resp.replace("%%announce_ms%%", String.valueOf(announcePerformanceMonitorService.avgMs()));
        resp = resp.replace("%%startup_date%%", announcePerformanceMonitorService.getStartTime().toString());
        resp = resp.replace("%%announce_count%%", String.valueOf(announcePerformanceMonitorService.getHandled()));
        resp = resp.replace("%%peers_list%%", peersJoiner.toString());
        resp = resp.replace("%%torrents_list%%", torrentsJoiner.toString());
        resp = resp.replace("%%debug_page_db_consumed%%", String.valueOf(dbTimeEnd));
        resp = resp.replace("%%debug_page_consumed%%", String.valueOf(System.currentTimeMillis() - startTime));
        resp = resp.replace("%%announce_job_avg%%", String.valueOf(announcePerformanceMonitorService.avgJobMs()));
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
            permissions.add(new Permission(0, "torrent:upload", false));
            permissions.add(new Permission(0, "torrent:scrape", true));
            permissions.add(new Permission(0, "torrent:list", false));
            permissions.add(new Permission(0, "torrent:view", false));
            permissions = permissions.stream().map(p -> permissionService.save(p)).toList();
            PromotionPolicy promotionPolicy =promotionService.save( new PromotionPolicy(0, "系统默认", 1.0d, 1.0d));
            UserGroup userGroup = userGroupService.save(new UserGroup(0, "default", "Lv.1 青铜", permissions, promotionPolicy));
            User user = userService.save(new User(0,
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
                    0,
                    new UUID(1,0).toString().replace("_","")));
            log.info("创建测试用户 1 成功");
            User user2 = userService.save(new User(0,
                    "test2@test.com",
                    "$2a$06$r6QixzXG/Y8mUtmCV7b70.Jp7qjOL2nONUJolzGmQPzVn2acoKLf6",
                    "TestUser2",
                    userGroup,
                    new UUID(0, 1).toString(),
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
                    0,
                    new UUID(2,0).toString().replace("_","")));
            log.info("创建测试用户 2 成功");
            Category category = categoryService.save(new Category(0,"test-category", "这是一个测试分类", "fa fa-book"));
            return "初始化基本数据库测试内容成功";
        } catch (Exception e) {
            log.error("Error: ", e);
            throw e;
        }
    }
}
