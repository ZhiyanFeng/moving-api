package com.lycheetech.movingapi.inventory.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InventoryResponse {
    private UUID id;
    private UUID moveId;
    private UUID itemId;
    private String itemName;
    private Integer quantity;
    private Boolean isFragile;
    private String customerNotes;
    private LocalDateTime createdAt;
}

