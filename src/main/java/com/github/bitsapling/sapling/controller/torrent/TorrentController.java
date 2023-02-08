package com.github.bitsapling.sapling.controller.torrent;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.entity.Category;
import com.github.bitsapling.sapling.entity.PromotionPolicy;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.entity.User;
import com.github.bitsapling.sapling.exception.APIGenericException;
import com.github.bitsapling.sapling.exception.EmptyTorrentFileException;
import com.github.bitsapling.sapling.exception.InvalidTorrentVersionException;
import com.github.bitsapling.sapling.exception.TorrentException;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import com.github.bitsapling.sapling.service.CategoryService;
import com.github.bitsapling.sapling.service.PromotionService;
import com.github.bitsapling.sapling.service.TorrentService;
import com.github.bitsapling.sapling.service.TransferHistoryService;
import com.github.bitsapling.sapling.service.UserService;
import com.github.bitsapling.sapling.util.TorrentParser;
import com.github.bitsapling.sapling.util.URLEncodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.github.bitsapling.sapling.exception.APIErrorCode.*;

@RestController
@RequestMapping("/torrent")
@Slf4j
public class TorrentController {
    @Autowired
    private TorrentService torrentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("torrentsDirectory")
    private File torrentsDirectory;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private TransferHistoryService transferHistoryService;

    @PostMapping("/upload")
    @SaCheckLogin
    @SaCheckPermission("torrent:upload")
    public ResponseEntity<ResponsePojo> upload(TorrentUploadForm form) throws IOException {
        if (StringUtils.isEmpty(form.getTitle())) {
            throw new APIGenericException(MISSING_PARAMETERS, "You must provide a title.");
        }
        if (StringUtils.isEmpty(form.getDescription())) {
            throw new APIGenericException(MISSING_PARAMETERS, "You must provide a description.");
        }
        if (form.getFile() == null || form.getFile().isEmpty()) {
            throw new APIGenericException(INVALID_TORRENT_FILE, "You must provide a valid torrent file.");
        }
        User user = userService.getUser(StpUtil.getLoginIdAsLong());
        Category category = categoryService.getCategory(form.getCategory());
        PromotionPolicy promotionPolicy = promotionService.getDefaultPromotionPolicy();
        if (category == null) {
            throw new APIGenericException(INVALID_CATEGORY, "Specified category not exists.");
        }
        if(form.isAnonymous()){
            StpUtil.checkPermission("torrent:publish_anonymous");
        }
        try {
            TorrentParser parser = new TorrentParser(form.getFile().getBytes());
            String infoHash = parser.getInfoHash();
            if (torrentService.getTorrent(infoHash) != null) {
                throw new APIGenericException(TORRENT_ALREADY_EXISTS, "The torrent's info_hash has been exists on this tracker.");
            }
            FileCopyUtils.copy(form.getFile().getInputStream(), Files.newOutputStream(new File(torrentsDirectory, infoHash + ".torrent").toPath()));
            Torrent torrent = new Torrent(0, infoHash, user, form.getTitle(),
                    form.getSubtitle(), parser.getTorrentFilesSize(),
                    0L, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()),
                    StpUtil.hasPermission("torrent:bypass_review"),
                    form.isAnonymous(),
                    category,
                    promotionPolicy, form.getDescription());
            torrent = torrentService.save(torrent);
            return ResponseEntity.ok()
                    .body(new TorrentUploadSuccess(torrent.getId(), parser.getInfoHash(), form.getFile()));
        } catch (EmptyTorrentFileException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, "This torrent is empty.");
        } catch (InvalidTorrentVersionException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, "V2 Torrent are not supported.");
        } catch (TorrentException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

    @GetMapping("/list")
    @SaCheckLogin
    @SaCheckPermission("torrent:list")
    public List<TorrentSearchResult> list() throws IOException {
       return torrentService.getAllTorrents().stream().map(TorrentSearchResult::new).toList();
    }

    @GetMapping("/view/{info_hash}")
    @SaCheckLogin
    @SaCheckPermission("torrent:view")
    public TorrentInfo view(@PathVariable("info_hash") String infoHash) throws IOException {
        Torrent torrent =  torrentService.getTorrent(infoHash);
        if(torrent == null){
            throw new APIGenericException(TORRENT_NOT_EXISTS, "This torrent not registered on this tracker");
        }
        return new TorrentInfo(torrent);
    }

    @GetMapping("/download")
    @SaCheckLogin
    @SaCheckPermission("torrent:download")
    public HttpEntity<?> download(@RequestParam Map<String, String> gets) throws IOException, TorrentException {
        String infoHash = gets.get("info_hash");
        if (StringUtils.isEmpty(infoHash)) {
            throw new APIGenericException(MISSING_PARAMETERS, "You must provide a info_hash.");
        }
        Torrent torrent = torrentService.getTorrent(infoHash);
        if (torrent == null) {
            throw new APIGenericException(TORRENT_NOT_EXISTS, "This torrent not registered on this tracker");
        }
        if (torrent.isUnderReview()) {
            StpUtil.checkPermission("torrent:download_review");
        }
        File torrentFile = new File(torrentsDirectory, infoHash + ".torrent");
        if (!torrentFile.exists()) {
            throw new APIGenericException(TORRENT_FILE_MISSING, "This torrent's file are missing on this tracker, please contact with system administrator.");
        }
        String publisher = torrent.getUser().getUsername();
        String publisherUrl = "http://demo.site/user/" + publisher;
        if (torrent.isAnonymous()) {
            publisher = null;
            publisherUrl = null;
        }
        TorrentParser parser = new TorrentParser(torrentFile);
        byte[] bytes = parser.rewrite(List.of("http://localhost:8081/announce"), "Demo Site", torrent.getUser().getPasskey(), publisher, publisherUrl);
        String fileName = "[Demo Site] " + torrent.getTitle() + ".torrent";
        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_TYPE, "application/x-bittorrent");
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncodeUtil.urlEncode(fileName, false));
        return new HttpEntity<>(bytes, header);
    }
    @Data
    static class TorrentSearchResult{
        private long id;
        private String infoHash;
        private ResultUserBean user;
        private String title;
        private String subTitle;
        private long size;
        private long finishes;
        private ResultCategoryBean category;
        private String promotionPolicy;

        public TorrentSearchResult(@NotNull Torrent torrent){
            this.id = torrent.getId();
            this.infoHash = torrent.getInfoHash();
            if(torrent.isAnonymous()){
                this.user = new ResultUserBean(null);
            }else{
                this.user = new ResultUserBean(torrent.getUser());
            }
            this.title = torrent.getTitle();
            this.subTitle = torrent.getSubTitle();
            this.size = torrent.getSize();
            this.finishes = torrent.getFinishes();
            this.category = new ResultCategoryBean(torrent.getCategory());
            this.promotionPolicy = torrent.getPromotionPolicy().getDisplayName();
        }
        @AllArgsConstructor
        @Data
        public static class ResultCategoryBean{
            private final long id;
            private final String slug;
            private final String name;
            protected ResultCategoryBean(Category category){
                this.id = category.getId();
                this.slug = category.getSlug();
                this.name = category.getName();
            }
        }
        @AllArgsConstructor
        @Data
        public static class ResultUserBean {
            private final long id;
            private final String username;
            protected ResultUserBean(@Nullable User user){
                if(user != null) {
                    this.id = user.getId();
                    this.username = user.getUsername();
                }else{
                    this.id = -1;
                    this.username = "Anonymous";
                }
            }
        }
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class TorrentUploadForm {
        private String title;
        private String subtitle;
        private String description;
        private String category;
        private List<String> tags;
        private boolean anonymous;
        private MultipartFile file;
    }

    @Getter
    static class TorrentUploadSuccess extends ResponsePojo {
        private final String originalName;
        private final String name;
        private final long size;
        private final String infoHash;
        private final long id;

        protected TorrentUploadSuccess(long id, @NotNull String infoHash, @Nullable MultipartFile multipartFile) {
            super(0);
            this.id = id;
            this.infoHash = infoHash;
            if (multipartFile != null) {
                this.originalName = multipartFile.getOriginalFilename();
                this.name = multipartFile.getName();
                this.size = multipartFile.getSize();
            } else {
                this.originalName = "null";
                this.name = "null";
                this.size = 0;
            }
        }

        @NotNull
        public String getInfoHash() {
            return infoHash;
        }

        public long getId() {
            return id;
        }
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class TorrentInfo {
        private long id;
        private String infoHash;
        private TorrentSearchResult.ResultUserBean user;
        private String title;
        private String subTitle;
        private long size;
        private long finishes;
        private Timestamp createdAt;
        private Timestamp updatedAt;
        private boolean underReview;
        private boolean anonymous;
        private TorrentSearchResult.ResultCategoryBean category;
        private PromotionPolicy promotionPolicy;
        private String description;

        public TorrentInfo(Torrent torrent){
            this.id = torrent.getId();
            this.infoHash = torrent.getInfoHash();
            if(torrent.isAnonymous()){
                this.user = new TorrentSearchResult.ResultUserBean(null);
            }else{
                this.user = new TorrentSearchResult.ResultUserBean(torrent.getUser());
            }
            this.title = torrent.getTitle();
            this.subTitle = torrent.getSubTitle();
            this.size = torrent.getSize();
            this.finishes = torrent.getFinishes();
            this.createdAt = torrent.getCreatedAt();
            this.updatedAt = torrent.getUpdatedAt();
            this.underReview = torrent.isUnderReview();
            this.anonymous = torrent.isAnonymous();
            this.category = new TorrentSearchResult.ResultCategoryBean(torrent.getCategory());
            this.promotionPolicy = torrent.getPromotionPolicy();
            this.description = torrent.getDescription();
        }
    }
}
