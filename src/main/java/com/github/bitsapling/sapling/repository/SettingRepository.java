package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.SettingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingRepository extends CrudRepository<SettingEntity, Long> {
    Optional<SettingEntity> findByKey(String key);

    void deleteByKey(String key);
}
