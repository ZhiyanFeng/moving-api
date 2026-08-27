package com.lycheetech.movingapi.lookup.movingservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovingServiceRepository extends JpaRepository<MovingService, UUID> {

    boolean existsByCode(String code);

    Optional<MovingService> findByCode(String code);

    List<MovingService> findAllByIsActiveTrue();
}

