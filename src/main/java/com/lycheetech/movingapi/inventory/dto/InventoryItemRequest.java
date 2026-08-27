package com.lycheetech.movingapi.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InventoryItemRequest {

    @NotBlank(message = "Item name is required")
    @Size(max = 100, message = "Item name must not exceed 100 characters")
    private String itemName;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    private String thumbnailUrl;

    @NotNull(message = "Cubic feet volume is required")
    private BigDecimal cubicFeetVolume;

    private Integer estimatedWeightLbs = 0;

    private Boolean isActive = true;
}

