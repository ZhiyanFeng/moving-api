package com.lycheetech.movingapi.rfq;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RfqStatusRepository extends JpaRepository<RfqStatus, UUID> {
    Optional<RfqStatus> findByName(String name);
}

