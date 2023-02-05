package com.github.bitsapling.sapling.controller.torrent;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.github.bitsapling.sapling.exception.EmptyTorrentFileException;
import com.github.bitsapling.sapling.exception.InvalidTorrentVersionException;
import com.github.bitsapling.sapling.exception.TorrentException;
import com.github.bitsapling.sapling.objects.ResponsePojo;
import com.github.bitsapling.sapling.service.TorrentService;
import com.github.bitsapling.sapling.util.TorrentParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/torrent")
@Slf4j
public class TorrentController {
    @Autowired
    private TorrentService torrentService;
    @Autowired
    @Qualifier("torrentsDirectory")
    private File torrentsDirectory;

    @PostMapping("/upload")
    @SaCheckLogin
    @SaCheckPermission("torrent:upload")
    public ResponseEntity<ResponsePojo> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if(file == null){
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
            if(multipartFile != null) {
                this.originalName = multipartFile.getOriginalFilename();
                this.name = multipartFile.getName();
                this.size = multipartFile.getSize();
            }else{
                this.originalName = "null";
                this.name = "null";
                this.size = 0;
            }
        }
    }
}
