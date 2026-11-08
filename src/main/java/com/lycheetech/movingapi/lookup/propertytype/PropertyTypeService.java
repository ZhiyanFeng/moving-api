package com.lycheetech.movingapi.lookup.propertytype;

import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.lookup.propertytype.dto.PropertyTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Read-only lookup service: property types are seeded via migration
 * (V2__populate_lookup_data.sql) rather than created/updated by end users.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PropertyTypeService {

    private final PropertyTypeRepository repository;

    public List<PropertyTypeResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public PropertyTypeResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public PropertyTypeResponse findBySlug(String slug) {
        return toResponse(repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Property type not found: " + slug)));
    }

    private PropertyType getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property type not found: " + id));
    }

    private PropertyTypeResponse toResponse(PropertyType entity) {
        return PropertyTypeResponse.builder()
                .id(entity.getId())
                .slug(entity.getSlug())
                .displayName(entity.getDisplayName())
                .category(entity.getCategory())
                .sectionName(entity.getSectionName())
                .roomCount(entity.getRoomCount())
                .isBasement(entity.isBasement())
                .estimatedCubicFeet(entity.getEstimatedCubicFeet())
                .displayOrder(entity.getDisplayOrder())
                .isActive(entity.isActive())
                .build();
    }
}

