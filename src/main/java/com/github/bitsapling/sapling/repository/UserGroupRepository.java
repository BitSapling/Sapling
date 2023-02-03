package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.UserGroupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends CrudRepository<UserGroupEntity, Long> {
}
