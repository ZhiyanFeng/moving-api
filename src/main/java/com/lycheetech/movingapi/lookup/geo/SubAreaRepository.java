package com.lycheetech.movingapi.lookup.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubAreaRepository extends JpaRepository<SubArea, UUID> {
    List<SubArea> findByMetroAreaId(UUID metroAreaId);
    Optional<SubArea> findByName(String name);
}

