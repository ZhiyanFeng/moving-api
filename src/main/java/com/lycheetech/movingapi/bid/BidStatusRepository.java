package com.lycheetech.movingapi.bid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidStatusRepository extends JpaRepository<BidStatus, UUID> {
    Optional<BidStatus> findByName(String name);
}

