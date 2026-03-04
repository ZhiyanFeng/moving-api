package com.lycheetech.movingapi.vehicle;

import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.vehicle.dto.VehicleRequest;
import com.lycheetech.movingapi.vehicle.dto.VehicleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.findAll()));
    }

    @GetMapping("/mover/{moverId}")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getByMover(@PathVariable UUID moverId) {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.findByMover(moverId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponse>> create(@Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Vehicle created", vehicleService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> update(@PathVariable UUID id,
                                                               @Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Vehicle updated", vehicleService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        vehicleService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

