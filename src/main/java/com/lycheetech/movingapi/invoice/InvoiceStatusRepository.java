package com.lycheetech.movingapi.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceStatusRepository extends JpaRepository<InvoiceStatus, UUID> {
    Optional<InvoiceStatus> findByName(String name);
}

