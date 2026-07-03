package com.primezat.demo.repository;

import com.primezat.demo.model.TravelAgencyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TravelAgencyInfoRepository extends JpaRepository<TravelAgencyInfo, Long> {
    Optional<TravelAgencyInfo> findByProjectId(Long projectId);
}
