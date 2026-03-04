package com.lycheetech.movingapi.inventory;

import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.inventory.dto.InventoryRequest;
import com.lycheetech.movingapi.inventory.dto.InventoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getByBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.findByBooking(bookingId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(inventoryService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryResponse>> create(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inventory item created", inventoryService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryResponse>> update(@PathVariable UUID id,
                                                                  @Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Inventory item updated", inventoryService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        inventoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

