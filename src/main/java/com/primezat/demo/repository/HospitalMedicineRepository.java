package com.primezat.demo.repository;

import com.primezat.demo.model.HospitalMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalMedicineRepository extends JpaRepository<HospitalMedicine, Long> {
    List<HospitalMedicine> findByProjectId(Long projectId);
}
