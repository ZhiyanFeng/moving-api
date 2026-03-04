package com.lycheetech.movingapi.vehicle;

import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.mover.Mover;
import com.lycheetech.movingapi.mover.MoverRepository;
import com.lycheetech.movingapi.vehicle.dto.VehicleRequest;
import com.lycheetech.movingapi.vehicle.dto.VehicleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleStatusRepository vehicleStatusRepository;
    private final MoverRepository moverRepository;

    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<VehicleResponse> findByMover(UUID moverId) {
        return vehicleRepository.findByMoverId(moverId).stream().map(this::toResponse).toList();
    }

    public VehicleResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    public VehicleResponse create(VehicleRequest request) {
        Mover mover = moverRepository.findById(request.getMoverId())
                .orElseThrow(() -> new ResourceNotFoundException("Mover not found: " + request.getMoverId()));
        VehicleStatus status = vehicleStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle status not found: " + request.getStatusId()));
        Vehicle vehicle = Vehicle.builder()
                .mover(mover)
                .status(status)
                .make(request.getMake())
                .model(request.getModel())
                .year(request.getYear())
                .licensePlate(request.getLicensePlate())
                .capacity(request.getCapacity())
                .build();
        return toResponse(vehicleRepository.save(vehicle));
    }

    public VehicleResponse update(UUID id, VehicleRequest request) {
        Vehicle vehicle = getOrThrow(id);
        VehicleStatus status = vehicleStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle status not found: " + request.getStatusId()));
        vehicle.setStatus(status);
        vehicle.setMake(request.getMake());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setLicensePlate(request.getLicensePlate());
        vehicle.setCapacity(request.getCapacity());
        return toResponse(vehicleRepository.save(vehicle));
    }

    public void delete(UUID id) {
        vehicleRepository.delete(getOrThrow(id));
    }

    private Vehicle getOrThrow(UUID id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
    }

    private VehicleResponse toResponse(Vehicle v) {
        return VehicleResponse.builder()
                .id(v.getId())
                .moverId(v.getMover().getId())
                .moverName(v.getMover().getName())
                .status(v.getStatus().getName())
                .make(v.getMake())
                .model(v.getModel())
                .year(v.getYear())
                .licensePlate(v.getLicensePlate())
                .capacity(v.getCapacity())
                .createdAt(v.getCreatedAt())
                .build();
    }
}

