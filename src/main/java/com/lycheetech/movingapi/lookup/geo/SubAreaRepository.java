package com.lycheetech.movingapi.lookup.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubAreaRepository extends JpaRepository<SubArea, Long> {
    List<SubArea> findByMetroAreaId(Long metroAreaId);
    Optional<SubArea> findByName(String name);
}

