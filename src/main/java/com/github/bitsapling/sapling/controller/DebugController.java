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
import com.github.bitsapling.sapling.type.PrivacyLevel;
import com.github.bitsapling.sapling.util.TorrentParser;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/debug")
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
        int debugPeers = 0;
        int debugTorrents = 0;
        long dbTimeStart = System.currentTimeMillis();
        for (Torrent entity : torrentRepository.findAll()) {
            debugTorrents++;
            torrentsJoiner.add(new DebugTorrent(entity).toString());
        }
        for (Peer peer : peersRepository.findAll()) {
            debugPeers++;
            peersJoiner.add(new DebugPeer(peer).toString());
        }
        long dbTimeEnd = System.currentTimeMillis() - dbTimeStart;
        String resp = page.replace("%%torrents_amount%%", String.valueOf(debugTorrents));
        resp = resp.replace("%%peers_amount%%", String.valueOf(debugPeers));
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

    @GetMapping("/parseTorrents")
    public String parseTorrent() throws IOException, TorrentException {
        StringJoiner joiner = new StringJoiner("\n");
        TorrentParser parser = new TorrentParser(new File("1.torrent"), true);
        joiner.add("File Size: " + parser.getTorrentFilesSize());
        joiner.add("Pieces Length: " + parser.getTorrentFilesSize());
        Map<String, Long> fileList = parser.getFileList();
        fileList.forEach((key, value) -> joiner.add("File -> " + key + ", Size -> " + value));
        return joiner.toString();
    }

    @GetMapping("/initTables")
    @Transactional
    public String initTables() {
        try {
            List<Permission> permissions = new ArrayList<>();
            permissions.add(new Permission(0, "torrent:announce", true));
            permissions.add(new Permission(0, "torrent:upload", false));
            permissions.add(new Permission(0, "torrent:scrape", true));
            permissions.add(new Permission(0, "torrent:list", false));
            permissions.add(new Permission(0, "torrent:view", false));
            permissions.add(new Permission(0, "torrent:download", false));
            permissions.add(new Permission(0, "torrent:download_review", false));
            permissions.add(new Permission(0, "torrent:search", false));
            permissions.add(new Permission(0, "torrent:thanks", false));
            permissions.add(new Permission(0, "torrent:publish_anonymous", false));
            permissions.add(new Permission(0, "torrent:bypass_review", false));
            permissions.add(new Permission(0, "promotion:list", false));
            permissions.add(new Permission(0, "category:list", false));
            permissions.add(new Permission(0, "feed:subscribe", false));

            permissions = permissions.stream().map(p -> permissionService.save(p)).toList();
            PromotionPolicy promotionPolicy = promotionService.save(new PromotionPolicy(0, "normal", "无促销", 1.0d, 1.0d));
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
                    new UUID(1, 0).toString().replace("_", ""),
                    PrivacyLevel.LOW));
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
                    new UUID(2, 0).toString().replace("_", ""),
                    PrivacyLevel.LOW));
            log.info("创建测试用户 2 成功");
            categoryService.save(new Category(0, "test-category", "这是一个测试分类", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "movie-sd", "电影/标清", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "movie-hd", "电影/高清", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "movie-dvd", "电影/DVDISO", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "movie-bluray", "电影/Blu-Ray", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "movie-remux", "电影/Remux", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "tv-sd", "影视剧/标清", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "tv-hd", "影视剧/高清", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "tv-dvd", "影视剧/DVDISO", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "tv-bluray", "影视剧/Blu-Ray", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "documentary-edu", "纪录片/教育", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "anime", "动画", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "sports", "体育", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "software", "软件", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "game", "游戏", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "ebook", "电子书", TODO_CATEGORY_IMG));
            categoryService.save(new Category(0, "other", "其它", TODO_CATEGORY_IMG));
            return "初始化基本数据库测试内容成功";
        } catch (Exception e) {
            log.error("Error: ", e);
            throw e;
        }
    }

    @Getter
    @ToString
    static class DebugTorrent {
        private final long id;
        private final String infoHash;
        private final String title;
        private final String subTitle;
        private final long size;
        private final Timestamp createdAt;
        private final Timestamp updatedAt;
        private final boolean underReview;
        private final boolean anonymous;
        private final Category category;
        private final PromotionPolicy promotionPolicy;
        private final String description;

        public DebugTorrent(Torrent torrent) {
            this.id = torrent.getId();
            this.infoHash = torrent.getInfoHash();
            this.title = torrent.getTitle();
            this.subTitle = torrent.getSubTitle();
            this.size = torrent.getSize();
            this.createdAt = torrent.getCreatedAt();
            this.updatedAt = torrent.getUpdatedAt();
            this.underReview = torrent.isUnderReview();
            this.anonymous = torrent.isAnonymous();
            this.category = torrent.getCategory();
            this.promotionPolicy = torrent.getPromotionPolicy();
            this.description = torrent.getDescription();

        }
    }

    @Getter
    @ToString
    static class DebugPeer {
        private final long id;
        private final String ip;
        private final int port;
        private final String infoHash;
        private final String userAgent;
        private final long uploaded;
        private final long downloaded;
        private final long left;
        private final boolean seeder;
        private final Timestamp updateAt;
        private final long seedingTime;

        public DebugPeer(Peer peer) {
            this.id = peer.getId();
            this.ip = peer.getIp();
            this.port = peer.getPort();
            this.infoHash = peer.getInfoHash();
            this.userAgent = peer.getUserAgent();
            this.uploaded = peer.getUploaded();
            this.downloaded = peer.getDownloaded();
            this.left = peer.getLeft();
            this.seeder = peer.isSeeder();
            this.updateAt = peer.getUpdateAt();
            this.seedingTime = peer.getSeedingTime();
        }
    }

    private static final String TODO_CATEGORY_IMG = "data:image/jpeg;base64,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";
}
