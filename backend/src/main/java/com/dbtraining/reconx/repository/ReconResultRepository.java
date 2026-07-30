package com.dbtraining.reconx.repository;

import com.dbtraining.reconx.repository.entity.ReconResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconResultRepository extends JpaRepository<ReconResultEntity, Long> {
}