package com.github.bitsapling.sapling.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "torrents",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"info_hash"})
        }
)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Torrent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    @Column(name = "info_hash", nullable = false, updatable = false)
    private String infoHash;
    @PrimaryKeyJoinColumn
    @Cascade({CascadeType.ALL})
    @ManyToOne
    @JsonBackReference
    private User user;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "sub_title", nullable = false)
    private String subTitle;
    @Column(name = "size", nullable = false, updatable = false)
    private long size;
    @Column(name = "finishes", nullable = false)
    private long finishes;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
    @Column(name = "under_review", nullable = false)
    private boolean underReview;
    @Column(name = "anonymous", nullable = false)
    private boolean anonymous;
    @ManyToOne
    @JsonBackReference
    @PrimaryKeyJoinColumn
    private Category category;
    @Cascade({CascadeType.ALL})
    @ManyToOne
    @PrimaryKeyJoinColumn
    @JsonBackReference
    private PromotionPolicy promotionPolicy;
    @Column(name = "description", nullable = false, columnDefinition = "mediumtext")
    private String description;
    @Cascade({CascadeType.ALL})
    @OneToMany
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private List<Tag> tag;

    public String getUsernameWithAnonymous(boolean canSeeAnonymous) {
        return canSeeAnonymous || !anonymous ? user.getUsername() : "Anonymous";
    }
}
