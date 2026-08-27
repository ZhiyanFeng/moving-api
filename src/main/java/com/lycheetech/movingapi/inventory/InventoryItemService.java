package com.lycheetech.movingapi.inventory;

import com.lycheetech.movingapi.common.exception.BadRequestException;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.inventory.dto.InventoryItemRequest;
import com.lycheetech.movingapi.inventory.dto.InventoryItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    public List<InventoryItemResponse> findAll() {
        return inventoryItemRepository.findAllByIsActiveTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    public InventoryItemResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public InventoryItemResponse create(InventoryItemRequest request) {
        if (inventoryItemRepository.existsByItemName(request.getItemName())) {
            throw new BadRequestException("Inventory item already exists with name: " + request.getItemName());
        }
        InventoryItem entity = InventoryItem.builder()
                .itemName(request.getItemName())
                .category(request.getCategory())
                .thumbnailUrl(request.getThumbnailUrl())
                .cubicFeetVolume(request.getCubicFeetVolume())
                .estimatedWeightLbs(request.getEstimatedWeightLbs())
                .isActive(request.getIsActive())
                .build();
        return toResponse(inventoryItemRepository.save(entity));
    }

    @Transactional
    public InventoryItemResponse update(UUID id, InventoryItemRequest request) {
        InventoryItem entity = getOrThrow(id);
        if (!entity.getItemName().equals(request.getItemName())
                && inventoryItemRepository.existsByItemName(request.getItemName())) {
            throw new BadRequestException("Inventory item already exists with name: " + request.getItemName());
        }
        entity.setItemName(request.getItemName());
        entity.setCategory(request.getCategory());
        entity.setThumbnailUrl(request.getThumbnailUrl());
        entity.setCubicFeetVolume(request.getCubicFeetVolume());
        entity.setEstimatedWeightLbs(request.getEstimatedWeightLbs());
        entity.setIsActive(request.getIsActive());
        return toResponse(inventoryItemRepository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        inventoryItemRepository.delete(getOrThrow(id));
    }

    private InventoryItem getOrThrow(UUID id) {
        return inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found: " + id));
    }

    private InventoryItemResponse toResponse(InventoryItem entity) {
        return InventoryItemResponse.builder()
                .id(entity.getId())
                .itemName(entity.getItemName())
                .category(entity.getCategory())
                .thumbnailUrl(entity.getThumbnailUrl())
                .cubicFeetVolume(entity.getCubicFeetVolume())
                .estimatedWeightLbs(entity.getEstimatedWeightLbs())
                .isActive(entity.getIsActive())
                .build();
    }
}

