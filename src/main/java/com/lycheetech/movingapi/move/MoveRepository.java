package com.lycheetech.movingapi.move;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MoveRepository extends JpaRepository<Move, UUID> {
    List<Move> findByMoverId(UUID moverId);
    List<Move> findByRfqUserId(UUID userId);
}

