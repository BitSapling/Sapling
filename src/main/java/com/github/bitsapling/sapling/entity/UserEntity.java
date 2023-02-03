package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"username"}),
                @UniqueConstraint(columnNames = {"email"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Transactional
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private long id;
    @Column(name = "email",nullable = false)
    private String email;
    @Column(name = "password",nullable = false)
    private String passwordHash;
    @Column(name = "username",nullable = false)
    private String username;
    @PrimaryKeyJoinColumn(name = "group")
    @OneToOne(cascade = CascadeType.MERGE ,fetch = FetchType.EAGER)
    private UserGroupEntity group;
    @Column(name = "passkey",nullable = false)
    private String passkey;
    @Column(name = "create_at",nullable = false)
    private Timestamp createdAt;
    @Column(name = "avatar",nullable = false)
    private String avatar;
    @Column(name = "custom_title",nullable = false)
    private String customTitle;
    @Column(name = "signature",nullable = false)
    private String signature;
    @Column(name = "language",nullable = false)
    private String language;
    @Column(name = "download_bandwidth",nullable = false)
    private String downloadBandwidth;
    @Column(name = "upload_bandwidth",nullable = false)
    private String uploadBandwidth;
    @Column(name = "downloaded",nullable = false)
    private long downloaded;
    @Column(name = "uploaded",nullable = false)
    private long uploaded;
    @Column(name = "real_downloaded",nullable = false)
    private long realDownloaded;
    @Column(name = "real_uploaded",nullable = false)
    private long realUploaded;
    @Column(name = "isp",nullable = false)
    private String isp;
    @Column(name = "karma",nullable = false)
    private BigDecimal karma;
    @Column(name = "invite_slot",nullable = false)
    private int inviteSlot;

    public UserGroupEntity getGroup() {
        return group;
    }
}
