package com.lycheetech.movingapi.inventory;

import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.inventory.dto.InventoryItemRequest;
import com.lycheetech.movingapi.inventory.dto.InventoryItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory-items")
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryItemResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(inventoryItemService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(inventoryItemService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryItemResponse>> create(
            @Valid @RequestBody InventoryItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inventory item created", inventoryItemService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody InventoryItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Inventory item updated", inventoryItemService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        inventoryItemService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

