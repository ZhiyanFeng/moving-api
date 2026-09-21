package com.lycheetech.movingapi.bid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidStatusRepository extends JpaRepository<BidStatus, Long> {
    Optional<BidStatus> findByName(String name);
}

