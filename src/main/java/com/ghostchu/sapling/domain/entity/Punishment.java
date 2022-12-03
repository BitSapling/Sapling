package com.ghostchu.sapling.domain.entity;

import com.ghostchu.sapling.domain.type.PunishmentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class Punishment {
    private final long punishmentId;
    @NotNull
    private PunishmentType type;
    private long userId;
    @NotNull
    private String reason;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
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
