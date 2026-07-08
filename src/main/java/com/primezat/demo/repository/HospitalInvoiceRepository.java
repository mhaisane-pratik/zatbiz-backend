package com.primezat.demo.repository;

import com.primezat.demo.model.HospitalInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalInvoiceRepository extends JpaRepository<HospitalInvoice, Long> {
    List<HospitalInvoice> findByProjectId(Long projectId);
}
