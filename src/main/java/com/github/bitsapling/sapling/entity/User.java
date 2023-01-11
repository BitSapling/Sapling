package com.github.bitsapling.sapling.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"username"})
        }
)
public class User {
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
    @Column(name = "rold",nullable = false)
    private String role;
    @Column(name = "passkey",nullable = false)
    private String passkey;
    @Column(name = "create_at",nullable = false)
    private Timestamp createdAt;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "custom_title")
    private String customTitle;
    @Column(name = "signature")
    private String signature;
    @Column(name = "language",nullable = false)
    private String language;
    @Column(name = "download_bandwidth",nullable = false)
    private String downloadBandwidth;
    @Column(name = "upload_bandwidth",nullable = false)
    private String uploadBandwidth;
    @Column(name = "isp",nullable = false)
    private String isp;
    @Column(name = "karma",nullable = false)
    private BigDecimal karma;
    @Column(name = "invite_slot",nullable = false)
    private int inviteSlot;
}
