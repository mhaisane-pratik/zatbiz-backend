package com.primezat.demo.repository;

import com.primezat.demo.model.TravelThemeSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TravelThemeSettingsRepository extends JpaRepository<TravelThemeSettings, Long> {
    Optional<TravelThemeSettings> findByProjectId(Long projectId);
}
