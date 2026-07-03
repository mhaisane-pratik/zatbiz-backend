package com.primezat.demo.repository;

import com.primezat.demo.model.TravelDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TravelDestinationRepository extends JpaRepository<TravelDestination, Long> {
    List<TravelDestination> findByProjectId(Long projectId);
}
