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
        return repository.findAllByIsActiveTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    public MovingServiceResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public MovingServiceResponse create(MovingServiceRequest request) {
        if (repository.existsByCode(request.getCode())) {
            throw new BadRequestException("Moving service already exists with code: " + request.getCode());
        }
        MovingService entity = MovingService.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public MovingServiceResponse update(UUID id, MovingServiceRequest request) {
        MovingService entity = getOrThrow(id);
        if (!entity.getCode().equals(request.getCode()) && repository.existsByCode(request.getCode())) {
            throw new BadRequestException("Moving service already exists with code: " + request.getCode());
        }
        entity.setCode(request.getCode());
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
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .active(entity.isActive())
                .build();
    }
}

