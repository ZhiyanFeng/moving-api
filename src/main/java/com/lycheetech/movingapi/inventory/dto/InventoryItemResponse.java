package com.lycheetech.movingapi.inventory.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class InventoryItemResponse {

    private UUID id;
    private String itemName;
    private String category;
    private String thumbnailUrl;
    private BigDecimal cubicFeetVolume;
    private Integer estimatedWeightLbs;
    private Boolean isActive;
}

