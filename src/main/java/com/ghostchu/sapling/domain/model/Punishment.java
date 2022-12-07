package com.ghostchu.sapling.domain.model;

import com.ghostchu.sapling.domain.type.PunishmentType;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Entity
@Table(name = "punishments")
public class Punishment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long punishmentId;
    @Column(name = "type")
    @NotNull
    private PunishmentType type;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "reason")
    @NotNull
    private String reason;
    @Column(name = "start_date")
    @NotNull
    private Date startDate;
    @Column(name = "end_date")
    @NotNull
    private Date endDate;
    @Column(name = "operator_id")
    private long operator;

    public Punishment(long punishmentId, @NotNull PunishmentType type, long userId, @NotNull String reason, @NotNull Date startDate, @NotNull Date endDate, long operator) {
        this.punishmentId = punishmentId;
        this.type = type;
        this.userId = userId;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.operator = operator;
    }

    public Punishment() {

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @NotNull
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull Date endDate) {
        this.endDate = endDate;
    }

    @NotNull
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotNull Date startDate) {
        this.startDate = startDate;
    }

    public long getPunishmentId() {
        return punishmentId;
    }

    public long getOperator() {
        return operator;
    }

    public void setOperator(long operator) {
        this.operator = operator;
    }

    @NotNull
    public PunishmentType getType() {
        return type;
    }

    public void setType(@NotNull PunishmentType type) {
        this.type = type;
    }

    @Nullable
    public String getReason() {
        return reason;
    }

    public void setReason(@NotNull String reason) {
        this.reason = reason;
    }
}
