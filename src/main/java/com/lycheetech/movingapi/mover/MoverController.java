package com.lycheetech.movingapi.mover;

import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.mover.dto.MoverRequest;
import com.lycheetech.movingapi.mover.dto.MoverResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/movers")
@RequiredArgsConstructor
public class MoverController {

    private final MoverService moverService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MoverResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(moverService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MoverResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(moverService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MoverResponse>> create(@Valid @RequestBody MoverRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Mover created", moverService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MoverResponse>> update(@PathVariable UUID id,
                                                             @Valid @RequestBody MoverRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Mover updated", moverService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        moverService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

