package com.lycheetech.movingapi.lookup.businessservice;

import com.lycheetech.movingapi.common.exception.BadRequestException;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.lookup.businessservice.dto.BusinessServiceRequest;
import com.lycheetech.movingapi.lookup.businessservice.dto.BusinessServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessServiceService {

    private final BusinessServiceRepository repository;

    public List<BusinessServiceResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public BusinessServiceResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public BusinessServiceResponse create(BusinessServiceRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new BadRequestException("Business service already exists with name: " + request.getName());
        }
        BusinessService entity = BusinessService.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public BusinessServiceResponse update(UUID id, BusinessServiceRequest request) {
        BusinessService entity = getOrThrow(id);
        if (!entity.getName().equals(request.getName()) && repository.existsByName(request.getName())) {
            throw new BadRequestException("Business service already exists with name: " + request.getName());
        }
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        repository.delete(getOrThrow(id));
    }

    private BusinessService getOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Business service not found: " + id));
    }

    private BusinessServiceResponse toResponse(BusinessService entity) {
        return BusinessServiceResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}

