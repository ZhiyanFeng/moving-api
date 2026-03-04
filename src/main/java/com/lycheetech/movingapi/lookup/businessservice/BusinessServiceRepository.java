package com.lycheetech.movingapi.lookup.businessservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "services", path = "business-services")
public interface BusinessServiceRepository extends JpaRepository<BusinessService, UUID> {
    Optional<BusinessService> findByName(String name);
}

