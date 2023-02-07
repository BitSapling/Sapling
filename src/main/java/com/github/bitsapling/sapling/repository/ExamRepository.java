package com.github.bitsapling.sapling.repository;

import com.github.bitsapling.sapling.entity.Exam;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends CrudRepository<Exam, Long> {

}
