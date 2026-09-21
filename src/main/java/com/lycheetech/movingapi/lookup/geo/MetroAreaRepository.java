package com.lycheetech.movingapi.lookup.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetroAreaRepository extends JpaRepository<MetroArea, Long> {
    List<MetroArea> findBySubdivisionId(Long subdivisionId);
    Optional<MetroArea> findByName(String name);
}

