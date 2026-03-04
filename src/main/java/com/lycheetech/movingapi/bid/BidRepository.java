package com.lycheetech.movingapi.bid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {
    List<Bid> findByRfqId(UUID rfqId);
    List<Bid> findByMoverId(UUID moverId);
}

