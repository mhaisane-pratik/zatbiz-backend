package com.primezat.demo.repository;

import com.primezat.demo.model.TravelVisa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TravelVisaRepository extends JpaRepository<TravelVisa, Long> {
    List<TravelVisa> findByProjectId(Long projectId);
}
