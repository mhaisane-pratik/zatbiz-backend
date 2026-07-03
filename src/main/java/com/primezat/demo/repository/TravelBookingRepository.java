package com.primezat.demo.repository;

import com.primezat.demo.model.TravelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TravelBookingRepository extends JpaRepository<TravelBooking, Long> {
    List<TravelBooking> findByProjectId(Long projectId);
}
