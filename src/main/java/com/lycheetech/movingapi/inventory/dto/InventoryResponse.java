package com.lycheetech.movingapi.inventory.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class InventoryResponse {
    private UUID id;
    private UUID bookingId;
    private String name;
    private Integer quantity;
    private String description;
}

