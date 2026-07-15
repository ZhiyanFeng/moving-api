package com.lycheetech.movingapi.lookup.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubdivisionRepository extends JpaRepository<Subdivision, UUID> {
    List<Subdivision> findByCountryId(UUID countryId);
    Optional<Subdivision> findByCode(String code);
}

