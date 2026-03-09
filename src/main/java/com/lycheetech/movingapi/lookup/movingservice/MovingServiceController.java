package com.lycheetech.movingapi.lookup.movingservice;

import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.lookup.movingservice.dto.MovingServiceRequest;
import com.lycheetech.movingapi.lookup.movingservice.dto.MovingServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moving-services")
@RequiredArgsConstructor
public class MovingServiceController {

    private final MovingServiceService movingServiceService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<MovingServiceResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(movingServiceService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovingServiceResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(movingServiceService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MovingServiceResponse>> create(
            @Valid @RequestBody MovingServiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Moving service created", movingServiceService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MovingServiceResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody MovingServiceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Moving service updated", movingServiceService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        movingServiceService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

