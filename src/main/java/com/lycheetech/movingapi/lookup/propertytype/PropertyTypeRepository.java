package com.lycheetech.movingapi.lookup.propertytype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyTypeRepository extends JpaRepository<PropertyType, Long> {

    Optional<PropertyType> findBySlug(String slug);

    List<PropertyType> findAllByIsActiveTrueOrderByDisplayOrderAsc();
}

