package com.github.bitsapling.sapling.controller.torrent;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.entity.Torrent;
import com.github.bitsapling.sapling.exception.APIGenericException;
import com.github.bitsapling.sapling.exception.EmptyTorrentFileException;
import com.github.bitsapling.sapling.exception.InvalidTorrentVersionException;
import com.github.bitsapling.sapling.exception.TorrentException;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import com.github.bitsapling.sapling.service.TorrentService;
import com.github.bitsapling.sapling.util.TorrentParser;
import com.github.bitsapling.sapling.util.URLEncodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    @Qualifier("torrentsDirectory")
    private File torrentsDirectory;
    @Autowired
    private HttpServletRequest request;

    @PostMapping("/upload")
    @SaCheckLogin
    @SaCheckPermission("torrent:upload")
    public ResponseEntity<ResponsePojo> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new APIGenericException(INVALID_TORRENT_FILE, "You must provide a file.");
        }
        try {
            TorrentParser parser = new TorrentParser(file.getBytes());
            String infoHash = parser.getInfoHash();
            if (torrentService.getTorrent(infoHash) != null) {
                throw new APIGenericException(TORRENT_ALREADY_EXISTS, "The torrent's info_hash has been exists on this tracker.");
            }
            FileCopyUtils.copy(file.getInputStream(), Files.newOutputStream(new File(torrentsDirectory, infoHash + ".torrent").toPath()));
            return ResponseEntity.ok()
                    .body(new TorrentUploadSuccess(parser.getInfoHash(), file));
        } catch (EmptyTorrentFileException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, "This torrent is empty.");
        } catch (InvalidTorrentVersionException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, "V2 Torrent are not supported.");
        } catch (TorrentException e) {
            throw new APIGenericException(INVALID_TORRENT_FILE, e.getClass().getSimpleName() + ":" + e.getMessage());
        }
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


    @Getter
    static class TorrentUploadSuccess extends ResponsePojo {
        private final String originalName;
        private final String name;
        private final long size;
        private final String infoHash;

        protected TorrentUploadSuccess(@NotNull String infoHash, @Nullable MultipartFile multipartFile) {
            super(0);
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
    }
}
