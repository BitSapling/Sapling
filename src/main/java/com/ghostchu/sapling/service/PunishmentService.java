package com.ghostchu.sapling.service;

import com.ghostchu.sapling.domain.entity.Punishment;
import com.ghostchu.sapling.domain.entity.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Collection;

@Service
public class PunishmentService {
    @Nullable
    public Punishment getPunishment(long punishmentId) {

    }

    @NotNull
    public Collection<Punishment> getPunishments(@NotNull User user) {

    }

    @NotNull
    public Collection<Punishment> getPunishments() {

    }

    public long appendPunishment(@NotNull User user, @NotNull Punishment punishment) {

    }

    public boolean removePunishment(@NotNull User user, @NotNull Punishment punishment) {

    }

    public boolean removePunishment(@NotNull User user, long punishmentId) {

    }

    public boolean isMuted(@NotNull User user) {
        return false;
    }

    public boolean isWarn(@NotNull User user) {
        return false;
    }

    public boolean hasDownloadPrivilege(@NotNull User user) {
        return false;
    }

    public boolean isBanned(@NotNull InetAddress ip) {
        return false;
    }

    public boolean isBanned(@NotNull String ipv4v6) {
        return false;
    }

    public boolean isBanned(@NotNull User user) {
        return false;
    }

    public boolean isBanned(@Nullable String... ips) {
        return false;
    }

    public boolean isBanned(int uid) {
        return false;
    }
}
