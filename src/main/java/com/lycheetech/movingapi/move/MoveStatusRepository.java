package com.lycheetech.movingapi.move;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MoveStatusRepository extends JpaRepository<MoveStatus, UUID> {
    Optional<MoveStatus> findByName(String name);
}

