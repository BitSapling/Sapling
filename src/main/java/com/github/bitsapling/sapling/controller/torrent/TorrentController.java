package com.github.bitsapling.sapling.controller.torrent;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.config.SiteBasicConfig;
import com.github.bitsapling.sapling.config.TrackerConfig;
import com.github.bitsapling.sapling.controller.torrent.form.TorrentUploadForm;
import com.github.bitsapling.sapling.controller.torrent.response.TorrentInfo;
import com.github.bitsapling.sapling.controller.torrent.response.TorrentSearchResult;
import com.github.bitsapling.sapling.controller.torrent.response.TorrentUploadSuccess;
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
import com.github.bitsapling.sapling.service.SettingService;
import com.github.bitsapling.sapling.service.TorrentService;
import com.github.bitsapling.sapling.service.TransferHistoryService;
import com.github.bitsapling.sapling.service.UserService;
import com.github.bitsapling.sapling.util.TorrentParser;
import com.github.bitsapling.sapling.util.URLEncodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private SettingService settingService;

    @PostMapping("/upload")
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
        if (form.isAnonymous()) {
            StpUtil.checkPermission("torrent:publish_anonymous");
        }
        try {
            TorrentParser parser = new TorrentParser(form.getFile().getBytes());
            String infoHash = parser.getInfoHash();
            if (torrentService.getTorrent(infoHash) != null) {
                throw new APIGenericException(TORRENT_ALREADY_EXISTS, "The torrent's info_hash has been exists on this tracker.");
            }
            FileCopyUtils.copy(form.getFile().getInputStream(), Files.newOutputStream(new File(torrentsDirectory, infoHash + ".torrent").toPath()));
            Torrent torrent = new Torrent(0, infoHash, user, form.getTitle(), form.getSubtitle(), parser.getTorrentFilesSize(), 0L, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), StpUtil.hasPermission("torrent:bypass_review"), form.isAnonymous(), category, promotionPolicy, form.getDescription());
            torrent = torrentService.save(torrent);
            return ResponseEntity.ok().body(new TorrentUploadSuccess(torrent.getId(), parser.getInfoHash(), form.getFile()));
        } catch (EmptyTorrentFileException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, "This torrent is empty.");
        } catch (InvalidTorrentVersionException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, "V2 Torrent are not supported.");
        } catch (TorrentException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

    @GetMapping("/list")
    @SaCheckPermission("torrent:list")
    public List<TorrentSearchResult> list() throws IOException {
        boolean permissionToSeeAnonymous = StpUtil.hasPermission("torrent:see_anonymous");
        return torrentService.getAllTorrents().stream().map(t -> new TorrentSearchResult(t, permissionToSeeAnonymous)).toList();
    }

    @GetMapping("/view/{info_hash}")
    @SaCheckPermission("torrent:view")
    public TorrentInfo view(@PathVariable("info_hash") String infoHash) {
        Torrent torrent = torrentService.getTorrent(infoHash);
        if (torrent == null) {
            throw new APIGenericException(TORRENT_NOT_EXISTS, "This torrent not registered on this tracker");
        }
        return new TorrentInfo(torrent);
    }

    @GetMapping("/download")
    @SaCheckPermission("torrent:download")
    public HttpEntity<?> download(@RequestParam Map<String, String> gets) throws IOException, TorrentException {
        SiteBasicConfig siteBasicConfig = settingService.get(SiteBasicConfig.getConfigKey(), SiteBasicConfig.class);
        TrackerConfig trackerConfig = settingService.get(TrackerConfig.getConfigKey(), TrackerConfig.class);
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
        String publisherUrl = siteBasicConfig.getSiteBaseURL() + "/user/" + publisher;
        if (torrent.isAnonymous()) {
            publisher = null;
            publisherUrl = null;
        }
        TorrentParser parser = new TorrentParser(torrentFile);
        byte[] bytes = parser.rewrite(trackerConfig.getTrackerURL(), trackerConfig.getTorrentPrefix(), torrent.getUser().getPasskey(), publisher, publisherUrl);
        String fileName = "[" + trackerConfig.getTorrentPrefix() + "] " + torrent.getTitle() + ".torrent";
        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_TYPE, "application/x-bittorrent");
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncodeUtil.urlEncode(fileName, false));
        return new HttpEntity<>(bytes, header);
    }
}
