package com.lycheetech.movingapi.rfq;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RfqRepository extends JpaRepository<Rfq, UUID> {
    List<Rfq> findByUserId(UUID userId);
    List<Rfq> findByStatusName(String statusName);
}

