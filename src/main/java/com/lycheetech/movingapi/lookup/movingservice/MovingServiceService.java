package com.lycheetech.movingapi.lookup.movingservice;

import com.lycheetech.movingapi.common.exception.BadRequestException;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.lookup.movingservice.dto.MovingServiceRequest;
import com.lycheetech.movingapi.lookup.movingservice.dto.MovingServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovingServiceService {

    private final MovingServiceRepository repository;

    public List<MovingServiceResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public MovingServiceResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public MovingServiceResponse create(MovingServiceRequest request) {
        if (repository.existsByName(request.getName())) {
            throw new BadRequestException("Moving service already exists with name: " + request.getName());
        }
        MovingService entity = MovingService.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public MovingServiceResponse update(UUID id, MovingServiceRequest request) {
        MovingService entity = getOrThrow(id);
        if (!entity.getName().equals(request.getName()) && repository.existsByName(request.getName())) {
            throw new BadRequestException("Moving service already exists with name: " + request.getName());
        }
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        repository.delete(getOrThrow(id));
    }

    private MovingService getOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Moving service not found: " + id));
    }

    private MovingServiceResponse toResponse(MovingService entity) {
        return MovingServiceResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}

