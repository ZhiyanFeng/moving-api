package com.lycheetech.movingapi.rfq;

import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.rfq.dto.RfqRequest;
import com.lycheetech.movingapi.rfq.dto.RfqResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rfqs")
@RequiredArgsConstructor
public class RfqController {

    private final RfqService rfqService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RfqResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(rfqService.findAll()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RfqResponse>>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(rfqService.findByUser(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RfqResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(rfqService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RfqResponse>> create(@Valid @RequestBody RfqRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("RFQ created", rfqService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RfqResponse>> update(@PathVariable UUID id,
                                                           @Valid @RequestBody RfqRequest request) {
        return ResponseEntity.ok(ApiResponse.success("RFQ updated", rfqService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        rfqService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

