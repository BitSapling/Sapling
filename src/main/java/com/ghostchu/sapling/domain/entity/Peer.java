package com.ghostchu.sapling.domain.entity;

import com.ghostchu.sapling.domain.type.PeerConnectableType;
import com.ghostchu.sapling.domain.type.PeerNetworkType;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "peers")
public class Peer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "client_slug")
    @Nullable
    private String clientSlug;
    @Column(name = "peer_id")
    @Nullable
    private String peerId;
    @Column(name = "network_type")
    @Nullable
    private PeerNetworkType networkType;
    @Column(name = "ipv4")
    @Nullable
    private String ipv4;
    @Column(name = "ipv6")
    @Nullable
    private String ipv6;
    @Column(name = "port")
    private int port;
    @Column(name = "uploaded")
    private long uploaded;
    @Column(name = "actual_uploaded")
    private long actualUploaded;
    @Column(name = "downloaded")
    private long downloaded;
    @Column(name = "actual_downloaded")
    private long actualDownloaded;
    @Column(name = "seeder")
    private boolean seeder;
    @Column(name = "start_time")
    @NotNull
    private Date startTime;
    @Column(name = "last_activity")
    @NotNull
    private Date lastActivity;
    @Column(name = "prev_activity")
    @NotNull
    private Date prevActivity;
    @Column(name = "connectable")
    @NotNull
    private PeerConnectableType connectable;
    @Column(name = "downloaded_offset")
    private long downloadedOffset;
    @Column(name = "uploaded_offset")
    private long uploadedOffset;
    @Column(name = "left")
    private long left;

    public Peer(long userId, @Nullable String peerId, @Nullable String clientSlug, @Nullable String ipv4, @Nullable String ipv6, int port, boolean seeder, @NotNull Date startTime, long left) {
        this.userId = userId;
        this.clientSlug = clientSlug;
        this.peerId = peerId;
        this.ipv4 = ipv4;
        this.ipv6 = ipv6;
        this.uploaded = 0;
        this.actualUploaded = 0;
        this.downloaded = 0;
        this.actualDownloaded = 0;
        this.seeder = seeder;
        this.startTime = startTime;
        this.lastActivity = startTime;
        this.prevActivity = startTime;
        this.connectable = PeerConnectableType.UNKNOWN;
        this.downloadedOffset = 0;
        this.uploadedOffset = 0;
        this.left = left;
        this.port = port;
        updateNetworkType();
    }

    public Peer() {

    }

    private void updateNetworkType() {
        if (ipv4 == null && ipv6 == null)
            throw new IllegalArgumentException("IPV4 and IPV6 cannot be null in same time");
        if (ipv6 != null)
            networkType = PeerNetworkType.IPV6;
        if (ipv4 != null)
            networkType = PeerNetworkType.IPV4;
        if (ipv4 != null && ipv6 != null)
            networkType = PeerNetworkType.BOTH;
    }

    public long getUserId() {
        return userId;
    }

    public @Nullable String getClientSlug() {
        return clientSlug;
    }

    public void setClientSlug(@NotNull String clientSlug) {
        this.clientSlug = clientSlug;
    }

    @Nullable
    public String getPeerId() {
        return peerId;
    }

    public void setPeerId(@Nullable String peerId) {
        this.peerId = peerId;
    }

    @Nullable
    public PeerNetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(@Nullable PeerNetworkType networkType) {
        this.networkType = networkType;
    }

    public @Nullable String getIpv4() {
        return ipv4;
    }

    public void setIpv4(@Nullable String ipv4) {
        this.ipv4 = ipv4;
        updateNetworkType();
    }

    public @Nullable String getIpv6() {
        return ipv6;
    }

    public void setIpv6(@Nullable String ipv6) {
        this.ipv6 = ipv6;
        updateNetworkType();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getUploaded() {
        return uploaded;
    }

    public void setUploaded(long uploaded) {
        this.uploaded = uploaded;
    }

    public long getActualUploaded() {
        return actualUploaded;
    }

    public void setActualUploaded(long actualUploaded) {
        this.actualUploaded = actualUploaded;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(long downloaded) {
        this.downloaded = downloaded;
    }

    public long getActualDownloaded() {
        return actualDownloaded;
    }

    public void setActualDownloaded(long actualDownloaded) {
        this.actualDownloaded = actualDownloaded;
    }

    public boolean isSeeder() {
        return seeder;
    }

    public void setSeeder(boolean seeder) {
        this.seeder = seeder;
    }

    public @NotNull Date getStartTime() {
        return startTime;
    }

    public void setStartTime(@NotNull Date startTime) {
        this.startTime = startTime;
    }

    public @NotNull Date getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(@NotNull Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    public @NotNull Date getPrevActivity() {
        return prevActivity;
    }

    public void setPrevActivity(@NotNull Date prevActivity) {
        this.prevActivity = prevActivity;
    }

    public @NotNull PeerConnectableType getConnectable() {
        return connectable;
    }

    public void setConnectable(@NotNull PeerConnectableType connectable) {
        this.connectable = connectable;
    }

    public long getDownloadedOffset() {
        return downloadedOffset;
    }

    public void setDownloadedOffset(long downloadedOffset) {
        this.downloadedOffset = downloadedOffset;
    }

    public long getUploadedOffset() {
        return uploadedOffset;
    }

    public void setUploadedOffset(long uploadedOffset) {
        this.uploadedOffset = uploadedOffset;
    }

    public long getLeft() {
        return left;
    }

    public void setLeft(long left) {
        this.left = left;
    }

    @Override
    public String toString() {
        return "Peer{" +
                "userId=" + userId +
                ", clientSlug='" + clientSlug + '\'' +
                ", peerId='" + peerId + '\'' +
                ", networkType=" + networkType +
                ", ipv4='" + ipv4 + '\'' +
                ", ipv6='" + ipv6 + '\'' +
                ", port=" + port +
                ", uploaded=" + uploaded +
                ", actualUploaded=" + actualUploaded +
                ", downloaded=" + downloaded +
                ", actualDownloaded=" + actualDownloaded +
                ", seeder=" + seeder +
                ", startTime=" + startTime +
                ", lastActivity=" + lastActivity +
                ", prevActivity=" + prevActivity +
                ", connectable=" + connectable +
                ", downloadedOffset=" + downloadedOffset +
                ", uploadedOffset=" + uploadedOffset +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peer peer = (Peer) o;
        return userId == peer.userId && port == peer.port && Objects.equals(clientSlug, peer.clientSlug) && Objects.equals(peerId, peer.peerId) && Objects.equals(ipv4, peer.ipv4) && Objects.equals(ipv6, peer.ipv6);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, clientSlug, peerId, ipv4, ipv6, port);
    }
}
