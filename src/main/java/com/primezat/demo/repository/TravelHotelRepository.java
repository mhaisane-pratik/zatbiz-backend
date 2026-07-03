package com.primezat.demo.repository;

import com.primezat.demo.model.TravelHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TravelHotelRepository extends JpaRepository<TravelHotel, Long> {
    List<TravelHotel> findByProjectId(Long projectId);
}
