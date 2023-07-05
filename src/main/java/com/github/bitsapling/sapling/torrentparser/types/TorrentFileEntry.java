package com.github.bitsapling.sapling.torrentparser.types;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.file.Path;

/**
 * .torrent 文件中的三个文件条目
 */
@AllArgsConstructor
@Data
public class TorrentFileEntry {
    private Path filePath;
    private long length;
}
