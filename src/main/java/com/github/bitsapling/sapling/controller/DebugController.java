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
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

    private static final String TODO_CATEGORY_IMG = "https://img1.imgtp.com/2023/02/13/M09Eg8vR.jpg";
}
