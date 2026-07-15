package com.lycheetech.movingapi.lookup.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MetroAreaRepository extends JpaRepository<MetroArea, UUID> {
    List<MetroArea> findBySubdivisionId(UUID subdivisionId);
    Optional<MetroArea> findByName(String name);
}

