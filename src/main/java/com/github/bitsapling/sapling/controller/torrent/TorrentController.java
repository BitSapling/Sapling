package com.github.bitsapling.sapling.controller.torrent;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.exception.EmptyTorrentFileException;
import com.github.bitsapling.sapling.exception.InvalidTorrentVersionException;
import com.github.bitsapling.sapling.exception.TorrentException;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import com.github.bitsapling.sapling.objects.Torrent;
import com.github.bitsapling.sapling.service.TorrentService;
import com.github.bitsapling.sapling.util.TorrentParser;
import com.github.bitsapling.sapling.util.URLEncodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

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
        if (file == null) {
            //noinspection ConstantValue
            return ResponseEntity.badRequest()
                    .body(new TorrentUploadEmptyFilePojo(file));
        }
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new TorrentUploadEmptyFilePojo(file));
        }
        try {
            TorrentParser parser = new TorrentParser(file.getBytes());
            String infoHash = parser.getInfoHash();
            if (torrentService.getTorrent(infoHash) != null) {
                return ResponseEntity.status(409)
                        .body(new TorrentUploadDuplicatePojo(file));
            }
            FileCopyUtils.copy(file.getInputStream(), Files.newOutputStream(new File(torrentsDirectory, infoHash + ".torrent").toPath()));
            return ResponseEntity.ok()
                    .body(new TorrentUploadSuccessPojo(parser.getInfoHash(), file));
        } catch (EmptyTorrentFileException e) {
            return ResponseEntity.badRequest()
                    .body(new TorrentUploadEmptyFilePojo(file));
        } catch (InvalidTorrentVersionException e) {
            return ResponseEntity.badRequest()
                    .body(new TorrentUploadInvalidTorrentVersionPojo(file));
        } catch (TorrentException e) {
            return ResponseEntity.badRequest()
                    .body(new TorrentUploadInvalidTorrentPojo(file));
        }
    }

    @GetMapping("/download")
    @SaCheckLogin
    @SaCheckPermission("torrent:download")
    public HttpEntity<?> download(@RequestParam Map<String, String> gets, HttpServletResponse response) throws IOException, TorrentException {
        log.debug("Scrape Query String: {}", request.getQueryString());
        log.debug("Gets: " + gets);
        String infoHash = gets.get("info_hash");
        if (StringUtils.isEmpty(infoHash)) {
            throw new IllegalArgumentException("You must provide info_hash.");
        }
        Torrent torrent = torrentService.getTorrent(infoHash);
        if (torrent == null) {
            return ResponseEntity
                    .status(404)
                    .body(new TorrentNotFound());
        }
        if (torrent.isUnderReview()) {
            StpUtil.checkPermission("torrent:download_review");
        }
        File torrentFile = new File(torrentsDirectory, infoHash + ".torrent");
        if (!torrentFile.exists()) {
            throw new IllegalStateException("The torrent file missing on this tracker, please contact tracker administrator");
        }
        String publisher = torrent.getUser().getUsername();
        String publisherUrl = "http://demo.site/user/" + publisher;
        if (torrent.isAnonymous()) {
            publisher = null;
            publisherUrl = null;
        }
        TorrentParser parser = new TorrentParser(torrentFile);
        byte[] bytes = parser.rewrite(List.of("http://localhost:8081/announce"), "Demo Site",torrent.getUser().getPasskey(), publisher, publisherUrl);
        String fileName = "[Demo Site] " + torrent.getTitle() + ".torrent";
        HttpHeaders header = new HttpHeaders();
        header.set(HttpHeaders.CONTENT_TYPE, "application/x-bittorrent");
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLEncodeUtil.urlEncode(fileName, false));
        return new HttpEntity<>(bytes, header);
    }

    @Getter
    static class TorrentNotFound extends ResponsePojo {
        private final String message = "Sorry, this torrent not registered on this tracker";

        protected TorrentNotFound() {
            super(20005);
        }
    }

    @Getter
    static class TorrentUploadDuplicatePojo extends TorrentUploadPojo {
        private final String message = "Sorry, this torrent already uploaded by others";

        protected TorrentUploadDuplicatePojo(@NotNull MultipartFile multipartFile) {
            super(20004, multipartFile);
        }
    }

    @Getter
    static class TorrentUploadInvalidTorrentVersionPojo extends TorrentUploadPojo {
        private final String message = "Sorry, we're currently only support V1 torrent version, please do not upload V2 or Hybird torrent file";

        protected TorrentUploadInvalidTorrentVersionPojo(@NotNull MultipartFile multipartFile) {
            super(20003, multipartFile);
        }
    }

    @Getter
    static class TorrentUploadInvalidTorrentPojo extends TorrentUploadPojo {
        private final String message = "The torrent file you uploaded not a valid .torrent file";

        protected TorrentUploadInvalidTorrentPojo(@NotNull MultipartFile multipartFile) {
            super(20002, multipartFile);
        }
    }

    @Getter
    static class TorrentUploadEmptyFilePojo extends TorrentUploadPojo {
        private final String message = "You can't upload a empty file";

        protected TorrentUploadEmptyFilePojo(@Nullable MultipartFile multipartFile) {
            super(20001, multipartFile);
        }
    }

    @Getter
    static class TorrentUploadSuccessPojo extends TorrentUploadPojo {
        private final String message = "Upload successfully";
        private final String infoHash;

        protected TorrentUploadSuccessPojo(@NotNull String infoHash, @NotNull MultipartFile multipartFile) {
            super(0, multipartFile);
            this.infoHash = infoHash;
        }
    }

    @Getter
    static class TorrentUploadPojo extends ResponsePojo {
        private final String originalName;
        private final String name;
        private final long size;

        protected TorrentUploadPojo(int code, @Nullable MultipartFile multipartFile) {
            super(code);
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
    }
}
