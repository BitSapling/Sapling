package com.github.bitsapling.sapling.torrentparser;

import com.dampcake.bencode.BencodeException;
import com.dampcake.bencode.Type;
import com.github.bitsapling.sapling.util.BencodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

@Slf4j
public class TorrentParserV2 {
    private static final List<String> V2_KEYS = List.of("piece layers", "files tree");
    private final byte[] data;
    private final Map<String, Long> fileList = new LinkedHashMap<>();
    private final boolean calcFiles;
    private Map<String, Object> dict;
    private long totalSize;

    public TorrentParserV2(File file, boolean calcFiles) throws IOException, BencodeException, ClassCastException {
        this.data = Files.readAllBytes(file.toPath());
        this.calcFiles = calcFiles;
        init();
    }

    public TorrentParserV2(InputStream stream, boolean calcFiles) throws IOException, BencodeException,
            ClassCastException {
        this.data = stream.readAllBytes();
        this.calcFiles = calcFiles;
        init();
    }

    public TorrentParserV2(URL url, boolean calcFiles) throws IOException, BencodeException, ClassCastException {
        try (InputStream stream = url.openStream()) {
            this.data = stream.readAllBytes();
        }
        this.calcFiles = calcFiles;
        init();
    }

    public TorrentParserV2(byte[] data, boolean calcFiles) throws BencodeException, ClassCastException {
        this.data = data;
        this.calcFiles = calcFiles;
        init();
    }

    private void init() throws ClassCastException {
        this.dict = BencodeUtil.bittorrent().decode(this.data, Type.DICTIONARY);
        //validate();
        if (calcFiles) {
            verifyAndCalcFiles();
        }
    }

    @SuppressWarnings("unchecked")
    public void verifyAndCalcFiles() {
        Queue<Map<String, Object>> queue = new LinkedList<>();
        Queue<String> files = new LinkedList<>();
        queue.add((Map<String, Object>) ((Map<String, Object>) this.dict.get("info")).get("file tree"));
        while (queue.size() > 0) {
            var cursor = queue.poll();
            var name = files.poll();
            if (!cursor.containsKey("")) {
                for (Map.Entry<String, Object> it : cursor.entrySet()) {
                    var map = (Map<String, Object>) it.getValue();
                    files.add(name + "/" + it.getKey());
                    queue.add(map);
                }
            } else {
                var item = (Map<String, Object>) cursor.get("");
                var length = (long) item.get("length");
                totalSize += length;
                fileList.put(name, length);
            }
        }
    }
}
