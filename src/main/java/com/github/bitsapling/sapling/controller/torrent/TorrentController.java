package com.github.bitsapling.sapling.controller.torrent;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.config.SiteBasicConfig;
import com.github.bitsapling.sapling.config.TrackerConfig;
import com.github.bitsapling.sapling.controller.dto.response.TorrentInfoResponseDTO;
import com.github.bitsapling.sapling.controller.torrent.dto.request.SearchTorrentRequestDTO;
import com.github.bitsapling.sapling.controller.torrent.dto.response.TorrentSearchResultResponseDTO;
import com.github.bitsapling.sapling.controller.torrent.dto.response.TorrentUploadSuccessResponseDTO;
import com.github.bitsapling.sapling.controller.torrent.form.TorrentUploadForm;
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
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;

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
    @Autowired
    private PolicyFactory sanitizeFactory;

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
        form.setDescription(sanitizeFactory.sanitize(form.getDescription()));
        User user = userService.getUser(StpUtil.getLoginIdAsLong());
        Category category = categoryService.getCategory(form.getCategory());
        PromotionPolicy promotionPolicy = promotionService.getDefaultPromotionPolicy();
        SiteBasicConfig siteBasicConfig = settingService.get(SiteBasicConfig.getConfigKey(), SiteBasicConfig.class);
        if (category == null) {
            throw new APIGenericException(INVALID_CATEGORY, "Specified category not exists.");
        }
        if (user == null) {
            throw new IllegalStateException("User cannot be null at this time");
        }
        String publisher = user.getUsername();
        String publisherUrl = siteBasicConfig.getSiteBaseURL() + "/user/" + user.getId();
        if (form.isAnonymous()) {
            StpUtil.checkPermission("torrent:publish_anonymous");
            publisher = "Anonymous";
            publisherUrl = siteBasicConfig.getSiteBaseURL();
        }
        try {
            byte[] torrentContent = TorrentParser.rewriteForTracker(form.getFile().getBytes(), siteBasicConfig.getSiteName(), publisher, publisherUrl);
            TorrentParser parser = new TorrentParser(torrentContent);
            String infoHash = parser.getInfoHash();
            if (torrentService.getTorrent(infoHash) != null) {
                throw new APIGenericException(TORRENT_ALREADY_EXISTS, "The torrent's info_hash has been exists on this tracker.");
            }
            Files.write(new File(torrentsDirectory, infoHash + ".torrent").toPath(), torrentContent);
            Torrent torrent = new Torrent(0, infoHash, user, form.getTitle(), form.getSubtitle(), parser.getTorrentFilesSize(), 0L, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), StpUtil.hasPermission("torrent:bypass_review"), form.isAnonymous(), category, promotionPolicy, form.getDescription());
            torrent = torrentService.save(torrent);
            return ResponseEntity.ok().body(new TorrentUploadSuccessResponseDTO(torrent.getId(), parser.getInfoHash(), form.getFile()));
        } catch (EmptyTorrentFileException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, "This torrent is empty.");
        } catch (InvalidTorrentVersionException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, "V2 Torrent are not supported.");
        } catch (TorrentException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

//    @GetMapping("/list")
//    @SaCheckPermission("torrent:list")
//    public List<TorrentSearchResultResponseDTO> list() throws IOException {
//        boolean permissionToSeeAnonymous = StpUtil.hasPermission("torrent:see_anonymous");
//        return torrentService.getAllTorrents().stream().map(t -> new TorrentSearchResultResponseDTO(t, permissionToSeeAnonymous)).toList();
//    }

    @GetMapping("/search")
    @SaCheckPermission("torrent:search")
    public TorrentSearchResultResponseDTO search(SearchTorrentRequestDTO searchRequestDTO) {
//        if (StringUtils.isEmpty(searchRequestDTO.getKeyword())) {
//            throw new APIGenericException(MISSING_PARAMETERS, "You must provide a keyword.");
//        }
        searchRequestDTO.setEntriesPerPage(Math.min(searchRequestDTO.getEntriesPerPage(), 300));
        Page<Torrent> torrents = torrentService.search(searchRequestDTO);
        return new TorrentSearchResultResponseDTO(torrents.getTotalElements(), torrents.getTotalPages(), torrents.getContent());
    }

    @GetMapping("/view/{info_hash}")
    @SaCheckPermission("torrent:view")
    public TorrentInfoResponseDTO view(@PathVariable("info_hash") String infoHash) {
        Torrent torrent = torrentService.getTorrent(infoHash);
        if (torrent == null) {
            throw new APIGenericException(TORRENT_NOT_EXISTS, "This torrent not registered on this tracker");
        }
        return new TorrentInfoResponseDTO(torrent);
    }

    @GetMapping("/download/{info_hash}")
    @SaCheckPermission("torrent:download")
    public HttpEntity<?> download(@PathVariable("info_hash") String infoHash) throws IOException, TorrentException {
        // SiteBasicConfig siteBasicConfig = settingService.get(SiteBasicConfig.getConfigKey(), SiteBasicConfig.class);
        TrackerConfig trackerConfig = settingService.get(TrackerConfig.getConfigKey(), TrackerConfig.class);
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
        byte[] bytes = TorrentParser.rewriteForUser(Files.readAllBytes(torrentFile.toPath()), trackerConfig.getTrackerURL(), torrent.getUser().getPasskey());
        String fileName = "[" + trackerConfig.getTorrentPrefix() + "] " + torrent.getTitle() + ".torrent";
        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_TYPE, "application/x-bittorrent");
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncodeUtil.urlEncode(fileName, false));
        return new HttpEntity<>(bytes, header);
    }
}
