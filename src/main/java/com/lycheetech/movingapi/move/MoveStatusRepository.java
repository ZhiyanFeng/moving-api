package com.lycheetech.movingapi.move;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoveStatusRepository extends JpaRepository<MoveStatus, Long> {
    Optional<MoveStatus> findByName(String name);
}

