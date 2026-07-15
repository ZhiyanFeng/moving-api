package com.lycheetech.movingapi.move;

import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.move.dto.MoveRequest;
import com.lycheetech.movingapi.move.dto.MoveResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moves")
@RequiredArgsConstructor
public class MoveController {

    private final MoveService moveService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MoveResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(moveService.findAll()));
    }

    @GetMapping("/mover/{moverId}")
    public ResponseEntity<ApiResponse<List<MoveResponse>>> getByMover(@PathVariable UUID moverId) {
        return ResponseEntity.ok(ApiResponse.success(moveService.findByMover(moverId)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<MoveResponse>>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(moveService.findByUser(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MoveResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(moveService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MoveResponse>> create(@Valid @RequestBody MoveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Move created", moveService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MoveResponse>> update(@PathVariable UUID id,
                                                             @Valid @RequestBody MoveRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Move updated", moveService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        moveService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

