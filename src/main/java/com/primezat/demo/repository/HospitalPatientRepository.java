package com.primezat.demo.repository;

import com.primezat.demo.model.HospitalPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalPatientRepository extends JpaRepository<HospitalPatient, Long> {
    List<HospitalPatient> findByProjectId(Long projectId);
    Optional<HospitalPatient> findByProjectIdAndEmail(Long projectId, String email);
}
