package com.primezat.demo.repository;

import com.primezat.demo.model.HospitalLabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalLabTestRepository extends JpaRepository<HospitalLabTest, Long> {
    List<HospitalLabTest> findByProjectId(Long projectId);
}
