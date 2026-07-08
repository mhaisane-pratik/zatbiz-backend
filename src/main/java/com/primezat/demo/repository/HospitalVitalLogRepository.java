package com.primezat.demo.repository;

import com.primezat.demo.model.HospitalVitalLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalVitalLogRepository extends JpaRepository<HospitalVitalLog, Long> {
    List<HospitalVitalLog> findByProjectId(Long projectId);
}
