package com.github.bitsapling.sapling.util;

import com.dampcake.bencode.BencodeException;
import com.dampcake.bencode.Type;
import com.github.bitsapling.sapling.exception.EmptyTorrentFileException;
import com.github.bitsapling.sapling.exception.InvalidTorrentFileException;
import com.github.bitsapling.sapling.exception.InvalidTorrentVerifyException;
import com.github.bitsapling.sapling.exception.InvalidTorrentVersionException;
import com.github.bitsapling.sapling.exception.TorrentException;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;

@Slf4j
public class TorrentParser {
    private static final List<String> V2_KEYS = List.of("piece layers", "files tree");
    private final byte[] data;
    private final Map<String, Long> fileList = new LinkedHashMap<>();
    private Map<String, Object> utf8Dict;
    private long totalSize;

    public TorrentParser(File file) throws IOException, BencodeException, TorrentException, ClassCastException {
        this.data = Files.readAllBytes(file.toPath());
        init();
    }

    public TorrentParser(InputStream stream) throws IOException, BencodeException, TorrentException, ClassCastException {
        this.data = stream.readAllBytes();
        init();
    }

    public TorrentParser(URL url) throws IOException, BencodeException, TorrentException, ClassCastException {
        try (InputStream stream = url.openStream()) {
            this.data = stream.readAllBytes();
        }
        init();
    }

    public TorrentParser(byte[] data) throws BencodeException, TorrentException, ClassCastException {
        this.data = data;
        init();
    }

    private void init() throws InvalidTorrentVerifyException, InvalidTorrentVersionException, InvalidTorrentFileException, ClassCastException, EmptyTorrentFileException {
        this.utf8Dict = BencodeUtil.bittorrent().decode(this.data, Type.DICTIONARY);
        validate();
        verifyAndCalcFiles();
    }

    private void validate() throws InvalidTorrentFileException, InvalidTorrentVersionException, InvalidTorrentVerifyException, ClassCastException {
        if (!this.utf8Dict.containsKey("info"))
            throw new InvalidTorrentFileException("Missing info key");
        if (isV2Torrent())
            throw new InvalidTorrentVersionException("version 2");
        @SuppressWarnings("unchecked")
        Map<String, Object> info = (Map<String, Object>) this.utf8Dict.get("info");
        if (!info.containsKey("piece length") || !(info.get("piece length") instanceof Number))
            throw new InvalidTorrentVerifyException("piece length", Number.class, info.get("piece length"));
        if (!info.containsKey("name") || !(info.get("name") instanceof String))
            throw new InvalidTorrentVerifyException("name", String.class, info.get("piece length"));
        if (!info.containsKey("pieces") || !(info.get("pieces") instanceof String))
            throw new InvalidTorrentVerifyException("pieces", String.class, info.get("pieces"));

    }

    private void verifyAndCalcFiles() throws InvalidTorrentVerifyException, EmptyTorrentFileException {
        this.fileList.clear();
        @SuppressWarnings("unchecked")
        Map<String, Object> info = (Map<String, Object>) this.utf8Dict.get("info");
        if (info.containsKey("length")) {
            // Single File Torrent
            String badEncodingFileName = (String) info.get("name");
            long size = (Long) info.get("length");
            String fileName = new String(badEncodingFileName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            this.fileList.put(fileName, size);
            return;
        }
        // Multiple files
        if (!info.containsKey("files") || !(info.get("files") instanceof List))
            throw new InvalidTorrentVerifyException("files", List.class, info.get("files"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> files = (List<Map<String, Object>>) info.get("files");
        if (files.isEmpty())
            throw new EmptyTorrentFileException();
        for (Map<String, Object> file : files) {
            if (file.get("length") == null || !(file.get("length") instanceof Number))
                throw new InvalidTorrentVerifyException("length", Number.class, file.get("length"));
            long size = (Long) file.get("length");
            List<String> path;
            if (file.get("path") != null) {
                //noinspection unchecked
                path = (List<String>) file.get("path");
            } else if (file.get("path.utf8") != null) {
                //noinspection unchecked
                path = (List<String>) file.get("path.utf8");
            } else {
                throw new InvalidTorrentVerifyException("path/path.utf8", List.class, file.get("path"));
            }
            List<String> convertedPath = path.stream()
                    .map(old -> new String(old.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8))
                    .toList();
            StringJoiner pathBuilder = new StringJoiner(File.separator);
            for (String s : convertedPath) {
                pathBuilder.add(s);
            }
            String finalPath = pathBuilder.toString();
            // BitComet stuff
            if (finalPath.contains("_____padding_file_")) {
                log.debug("Skipped {} because it's a BitComet padding file.", finalPath);
                continue;
            }
            this.fileList.put(finalPath, size);
        }
        totalSize = this.fileList.values().stream().mapToLong(v -> v).sum();
    }

    public long getTorrentFilesSize() {
        return totalSize;
    }

    private boolean isV2Torrent() throws ClassCastException {
        @SuppressWarnings("unchecked")
        Map<String, Object> info = (Map<String, Object>) this.utf8Dict.get("info");
        for (String v2Key : V2_KEYS) {
            if (info.containsKey(v2Key))
                return true;
        }
        if (info.containsKey("meta version"))
            return info.get("meta version").equals(2);
        return false;
    }

    public Map<String, Long> getFileList() {
        return fileList;
    }

    public Map<String, Object> getDict() {
        return utf8Dict;
    }

    @NotNull
    public String getInfoHash() {
        Map<String, Object> infoHashDat = BencodeUtil.bittorrent().decode(this.data, Type.DICTIONARY);
        //noinspection deprecation
        return Hashing.sha1().hashBytes(BencodeUtil.bittorrent().encode((Map<?, ?>) infoHashDat.get("info"))).toString().toLowerCase(Locale.ROOT);
    }

    public byte @NotNull [] rewrite(@NotNull List<String> trackers, @NotNull String siteName, @NotNull String passkey, @Nullable String publisher,
                                    @Nullable String publisherUrl) {
        Map<String, Object> editDict = BencodeUtil.bittorrent().decode(this.data, Type.DICTIONARY);
        @SuppressWarnings("unchecked")
        Map<String, Object> info = (Map<String, Object>) editDict.get("info");
        info.put("private", 1);
        info.put("created by", "BitSapling-TorrentParser/1.0; " + info.getOrDefault("created by", "N/A"));
        info.put("source", "[" + siteName + "] " + info.getOrDefault("source", "N/A"));
        editDict.put("info", info);
        for (int i = 0; i < trackers.size(); i++) {
            if (i == 0) {
                editDict.put("announce", trackers.get(0) + "?passkey=" + passkey);
            }
            editDict.put("announce-list", trackers.get(i) + "?passkey=" + passkey);
        }
        if (publisher != null)
            editDict.put("publisher", publisher);
        if (publisherUrl != null)
            editDict.put("publisher-url", publisherUrl);
        editDict.remove("nodes");
        return BencodeUtil.bittorrent().encode(editDict);
    }
}
