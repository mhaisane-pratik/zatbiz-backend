package com.primezat.demo.repository;

import com.primezat.demo.model.TravelFlight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TravelFlightRepository extends JpaRepository<TravelFlight, Long> {
    List<TravelFlight> findByProjectId(Long projectId);
}
