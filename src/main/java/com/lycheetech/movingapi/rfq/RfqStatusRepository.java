package com.lycheetech.movingapi.rfq;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RfqStatusRepository extends JpaRepository<RfqStatus, Long> {
    Optional<RfqStatus> findByName(String name);
}

