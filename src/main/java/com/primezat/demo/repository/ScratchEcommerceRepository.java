package com.primezat.demo.repository;

import com.primezat.demo.model.ScratchEcommerce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScratchEcommerceRepository extends JpaRepository<ScratchEcommerce, Long> {
    Optional<ScratchEcommerce> findByProjectId(Long projectId);
}
