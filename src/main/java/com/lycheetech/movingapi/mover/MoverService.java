package com.lycheetech.movingapi.mover;

import com.lycheetech.movingapi.common.exception.BadRequestException;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.lookup.region.Region;
import com.lycheetech.movingapi.lookup.region.RegionRepository;
import com.lycheetech.movingapi.lookup.movingservice.MovingService;
import com.lycheetech.movingapi.lookup.movingservice.MovingServiceRepository;
import com.lycheetech.movingapi.mover.dto.MoverRequest;
import com.lycheetech.movingapi.mover.dto.MoverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoverService {

    private final MoverRepository moverRepository;
    private final RegionRepository regionRepository;
    private final MovingServiceRepository movingServiceRepository;

    public List<MoverResponse> findAll() {
        return moverRepository.findAll().stream().map(this::toResponse).toList();
    }

    public MoverResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public MoverResponse create(MoverRequest request) {
        if (moverRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use: " + request.getEmail());
        }
        Mover mover = Mover.builder()
                .name(request.getName())
                .companyName(request.getCompanyName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .description(request.getDescription())
                .avatarUrl(request.getAvatarUrl())
                .vehicleUrl(request.getVehicleUrl())
                .regions(resolveRegions(request.getRegionIds()))
                .services(resolveServices(request.getServiceIds()))
                .build();
        return toResponse(moverRepository.save(mover));
    }

    @Transactional
    public MoverResponse update(UUID id, MoverRequest request) {
        Mover mover = getOrThrow(id);
        mover.setName(request.getName());
        mover.setCompanyName(request.getCompanyName());
        mover.setEmail(request.getEmail());
        mover.setPhoneNumber(request.getPhoneNumber());
        mover.setAddress(request.getAddress());
        mover.setDescription(request.getDescription());
        mover.setAvatarUrl(request.getAvatarUrl());
        mover.setVehicleUrl(request.getVehicleUrl());
        mover.setRegions(resolveRegions(request.getRegionIds()));
        mover.setServices(resolveServices(request.getServiceIds()));
        return toResponse(moverRepository.save(mover));
    }

    public void delete(UUID id) {
        moverRepository.delete(getOrThrow(id));
    }

    private Mover getOrThrow(UUID id) {
        return moverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mover not found: " + id));
    }

    private Set<Region> resolveRegions(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return new HashSet<>(regionRepository.findAllById(ids));
    }

    private Set<MovingService> resolveServices(Set<UUID> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return new HashSet<>(movingServiceRepository.findAllById(ids));
    }

    private MoverResponse toResponse(Mover mover) {
        return MoverResponse.builder()
                .id(mover.getId())
                .name(mover.getName())
                .companyName(mover.getCompanyName())
                .email(mover.getEmail())
                .phoneNumber(mover.getPhoneNumber())
                .address(mover.getAddress())
                .description(mover.getDescription())
                .avatarUrl(mover.getAvatarUrl())
                .vehicleUrl(mover.getVehicleUrl())
                .regions(mover.getRegions().stream().map(Region::getName).collect(Collectors.toSet()))
                .services(mover.getServices().stream()
                        .map(MovingService::getName)
                        .collect(Collectors.toSet()))
                .createdAt(mover.getCreatedAt())
                .build();
    }
}

