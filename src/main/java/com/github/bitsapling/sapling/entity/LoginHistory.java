package com.github.bitsapling.sapling.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.bitsapling.sapling.type.LoginType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.sql.Timestamp;

@Entity
@Table(name = "login_history",
        indexes = {
                @Index(columnList = "time")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;
    @PrimaryKeyJoinColumn(name = "group", referencedColumnName = "id")
    @Cascade({CascadeType.ALL})
    @ManyToOne
    @JsonBackReference
    private User user;
    @Column(name = "time", nullable = false, updatable = false)
    private Timestamp loginTime;
    @Column(name = "type", nullable = false, updatable = false)
    private LoginType loginType;
    @Column(name = "ip_address", updatable = false)
    private String ipAddress;
    @Column(name = "user_agent", updatable = false)
    private String userAgent;
    @Column(name = "location", updatable = false)
    private String location;
}
