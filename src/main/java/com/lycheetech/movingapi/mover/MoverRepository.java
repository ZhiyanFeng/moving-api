package com.lycheetech.movingapi.mover;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MoverRepository extends JpaRepository<Mover, UUID> {
    Optional<Mover> findByEmail(String email);
    boolean existsByEmail(String email);
}

