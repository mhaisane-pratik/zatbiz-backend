package com.primezat.demo.repository;

import com.primezat.demo.model.HospitalAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalAppointmentRepository extends JpaRepository<HospitalAppointment, Long> {
    List<HospitalAppointment> findByProjectId(Long projectId);
    List<HospitalAppointment> findByProjectIdAndPatientEmail(Long projectId, String email);
}
