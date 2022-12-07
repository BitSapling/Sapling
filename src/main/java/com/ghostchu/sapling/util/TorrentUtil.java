package com.ghostchu.sapling.util;

import com.dampcake.bencode.Bencode;
import com.dampcake.bencode.BencodeException;
import com.dampcake.bencode.Type;
import com.ghostchu.sapling.exception.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class TorrentUtil {
    private static final List<String> V2_KEYS = List.of("piece layers", "files tree");
    private final Bencode bencode = new Bencode(StandardCharsets.UTF_8);
    private final Bencode bencodeInfoHash = new Bencode(StandardCharsets.ISO_8859_1);
    private final byte[] data;
    private final Map<String, Long> fileList = new LinkedHashMap<>();
    private Map<String, Object> dict;
    private long totalSize;

    public TorrentUtil(File file) throws IOException, BencodeException, TorrentException {
        this.data = Files.readAllBytes(file.toPath());
        init();
    }

    public TorrentUtil(InputStream stream) throws IOException, BencodeException, TorrentException {
        this.data = stream.readAllBytes();
        init();
    }

    public TorrentUtil(URL url) throws IOException, BencodeException, TorrentException {
        try (InputStream stream = url.openStream()) {
            this.data = stream.readAllBytes();
        }
        init();
    }

    public TorrentUtil(byte[] data) throws BencodeException, TorrentException {
        this.data = data;
        init();
    }

    private void init() throws BencodeException, TorrentException {
        this.dict = bencode.decode(this.data, Type.DICTIONARY);
        validate();
    }

    private void verifyAndCalcFiles() throws InvalidTorrentVerifyException, EmptyTorrentFileException {
        this.fileList.clear();
        @SuppressWarnings("unchecked")
        Map<String, Object> info = (Map<String, Object>) this.dict.get("info");
        if (info.containsKey("length")) {
            // Single File Torrent
            this.fileList.put((String) info.get("name"), (Long) info.get("length"));
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
            if (file.get("path") == null && file.get("path.utf-8") == null)
                throw new InvalidTorrentVerifyException("path/path.utf8", List.class, file.get("path"));
            @SuppressWarnings("unchecked")
            List<String> path = file.get("path") != null ? (List<String>) file.get("path") : (List<String>) file.get("path.utf-8");
            StringJoiner pathBuilder = new StringJoiner(File.separator);
            for (String s : path) {
                pathBuilder.add(s);
            }
            this.fileList.put(pathBuilder.toString(), size);
        }
        totalSize = this.fileList.values().stream().mapToLong(v -> v).sum();
    }

    public long getTorrentFilesSize() {
        return totalSize;
    }

    private void validate() throws InvalidTorrentVerifyException, InvalidTorrentVersionException, InvalidTorrentFileException, EmptyTorrentFileException {
        if (this.dict == null)
            throw new InvalidTorrentFileException("Bencode decode failed");
        if (!this.dict.containsKey("info"))
            throw new InvalidTorrentFileException("Missing info key");
        if (isV2Torrent())
            throw new InvalidTorrentVersionException("version 2");
        @SuppressWarnings("unchecked")
        Map<String, Object> info = (Map<String, Object>) this.dict.get("info");
        if (!info.containsKey("piece length") || !(info.get("piece length") instanceof Number))
            throw new InvalidTorrentVerifyException("piece length", Number.class, info.get("piece length"));
        if (!info.containsKey("name") || !(info.get("name") instanceof String))
            throw new InvalidTorrentVerifyException("name", String.class, info.get("piece length"));
        if (!info.containsKey("pieces") || !(info.get("pieces") instanceof String))
            throw new InvalidTorrentVerifyException("pieces", String.class, info.get("pieces"));
//        if (((String) info.get("pieces")).length() % 20 != 0)
//            throw new InvalidTorrentPiecesException(((String) info.get("pieces")).length());
        verifyAndCalcFiles();
    }


    private boolean isV2Torrent() {
        @SuppressWarnings("unchecked")
        Map<String, Object> info = (Map<String, Object>) this.dict.get("info");
        for (String v2Key : V2_KEYS) {
            if (info.containsKey(v2Key))
                return true;
        }
        if (info.containsKey("meta version"))
            return info.get("meta version").equals(2);
        return false;
    }

    /**
     * Rewrite the torrent.
     * This operation will remove all sensitive information, make torrent private and replace the tracker
     *
     * @param announce The tracker
     */
    public void rewrite(@NotNull String announce, @NotNull List<String> backupAnnounce, @NotNull String baseUrl, @NotNull String siteName) {
        @SuppressWarnings("unchecked")
        Map<String, Object> info = (Map<String, Object>) this.dict.get("info");
        info.put("private", 1);
        info.put("source", "[" + baseUrl + "] " + siteName);
        this.dict.put("info", info);
        this.dict.put("announce", announce);
        this.dict.put("announce-list", backupAnnounce);
        this.dict.remove("nodes");
    }

    @NotNull
    public String getInfoHash() {
        Map<String, Object> infoHashDat = bencodeInfoHash.decode(this.data, Type.DICTIONARY);
        return HashUtil.sha1(bencodeInfoHash.encode((Map<?, ?>) infoHashDat.get("info")));
    }

    public byte[] save() {
        return bencode.encode(this.dict);
    }

    public void save(@NotNull File file) throws IOException {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (file.exists()) file.delete();
        Files.copy(new ByteArrayInputStream(save()), file.toPath());
    }

    public void save(@NotNull OutputStream os, boolean autoClose) throws IOException {
        os.write(save());
        os.flush();
        if (autoClose)
            os.close();
    }
}
