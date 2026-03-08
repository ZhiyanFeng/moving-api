package com.lycheetech.movingapi.lookup.businessservice;

import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.lookup.businessservice.dto.BusinessServiceRequest;
import com.lycheetech.movingapi.lookup.businessservice.dto.BusinessServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/business-services")
@RequiredArgsConstructor
public class BusinessServiceController {

    private final BusinessServiceService businessServiceService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<BusinessServiceResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(businessServiceService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BusinessServiceResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(businessServiceService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BusinessServiceResponse>> create(
            @Valid @RequestBody BusinessServiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Business service created", businessServiceService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BusinessServiceResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody BusinessServiceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Business service updated", businessServiceService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        businessServiceService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

