package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.PromotionPolicy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionPolicyRepository extends CrudRepository<PromotionPolicy, Long> {

}
