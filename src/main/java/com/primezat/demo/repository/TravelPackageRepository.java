package com.primezat.demo.repository;

import com.primezat.demo.model.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {
    List<TravelPackage> findByProjectId(Long projectId);
}
