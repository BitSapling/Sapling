package com.github.bitsapling.sapling.module.tracker;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.github.bitsapling.sapling.controller.ApiResponse;
import com.github.bitsapling.sapling.module.torrent.Torrent;
import com.github.bitsapling.sapling.module.torrent.TorrentService;
import com.github.bitsapling.sapling.module.tracker.dto.PeerLevelAdminReadOnlyDTO;
import com.github.bitsapling.sapling.module.tracker.dto.PeerLevelNormalReadOnlyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/peer")
@Slf4j
@Validated
public class PeerController {
    @Autowired
    private PeerService service;
    @Autowired
    private TorrentService torrentService;

    @GetMapping("/{torrentInfoHash}")
    @SaCheckPermission("peer:read")
    public ApiResponse<?> getPeers(@PathVariable("torrentInfoHash") String torrentInfoHash) {
        Torrent torrent = torrentService.getTorrentByInfoHash(torrentInfoHash);
        if (torrent == null) {
            return new ApiResponse<>(404, "Torrent " + torrentInfoHash + " not found.");
        }
        List<Peer> peers = service.getPeersByTorrent(torrent.getId());
        if (StpUtil.hasPermission("peer:admin-read")) {
            return new ApiResponse<>(peers.stream().map(peer -> (PeerLevelAdminReadOnlyDTO) peer).toList());
        } else {
            return new ApiResponse<>(peers.stream().map(PeerLevelNormalReadOnlyDTO::new).toList());
        }
    }
}
