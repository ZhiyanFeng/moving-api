package com.lycheetech.movingapi.inventory;

import com.lycheetech.movingapi.move.MoveRepository;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.inventory.dto.InventoryRequest;
import com.lycheetech.movingapi.inventory.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MoveRepository moveRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public List<InventoryResponse> findByMove(UUID moveId) {
        return inventoryRepository.findByMoveId(moveId).stream().map(this::toResponse).toList();
    }

    public InventoryResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    public InventoryResponse create(InventoryRequest request) {
        var move = moveRepository.findById(request.getMoveId())
                .orElseThrow(() -> new ResourceNotFoundException("Move not found: " + request.getMoveId()));
        var item = inventoryItemRepository.findById(request.getItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item type not found: " + request.getItemId()));
        Inventory inventory = Inventory.builder()
                .move(move)
                .item(item)
                .quantity(request.getQuantity())
                .isFragile(request.getIsFragile())
                .customerNotes(request.getCustomerNotes())
                .build();
        return toResponse(inventoryRepository.save(inventory));
    }

    public InventoryResponse update(UUID id, InventoryRequest request) {
        Inventory inventory = getOrThrow(id);
        if (request.getItemId() != null && !request.getItemId().equals(inventory.getItem().getId())) {
            var item = inventoryItemRepository.findById(request.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory item type not found: " + request.getItemId()));
            inventory.setItem(item);
        }
        inventory.setQuantity(request.getQuantity());
        inventory.setIsFragile(request.getIsFragile());
        inventory.setCustomerNotes(request.getCustomerNotes());
        return toResponse(inventoryRepository.save(inventory));
    }

    public void delete(UUID id) {
        inventoryRepository.delete(getOrThrow(id));
    }

    private Inventory getOrThrow(UUID id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found: " + id));
    }

    private InventoryResponse toResponse(Inventory i) {
        return InventoryResponse.builder()
                .id(i.getId())
                .moveId(i.getMove().getId())
                .itemId(i.getItem().getId())
                .itemName(i.getItem().getItemName())
                .quantity(i.getQuantity())
                .isFragile(i.getIsFragile())
                .customerNotes(i.getCustomerNotes())
                .createdAt(i.getCreatedAt())
                .build();
    }
}

