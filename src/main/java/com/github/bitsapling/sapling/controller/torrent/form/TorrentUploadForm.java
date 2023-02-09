package com.github.bitsapling.sapling.controller.torrent.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TorrentUploadForm {
    private String title;
    private String subtitle;
    private String description;
    private String category;
    private List<String> tag;
    private boolean anonymous;
    private MultipartFile file;
}
