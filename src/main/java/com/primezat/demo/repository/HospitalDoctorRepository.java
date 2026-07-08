package com.primezat.demo.repository;

import com.primezat.demo.model.HospitalDoctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalDoctorRepository extends JpaRepository<HospitalDoctor, Long> {
    List<HospitalDoctor> findByProjectId(Long projectId);
}
