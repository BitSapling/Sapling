package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.LoginHistory;
import com.github.bitsapling.sapling.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface LoginHistoryRepository extends CrudRepository<LoginHistory, Long> {
    List<LoginHistory> findAllByIpAddress(@NotNull String ipAddress);
    List<LoginHistory> findAllByUserAgent(@NotNull String userAgent);

    List<LoginHistory> findAllByLoginTimeBetween(@NotNull Timestamp start, @NotNull Timestamp end);

    List<LoginHistory> findAllByUserOrderByLoginTimeDesc(@NotNull User user);
}
