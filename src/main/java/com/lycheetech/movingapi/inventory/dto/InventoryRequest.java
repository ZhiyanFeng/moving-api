package com.lycheetech.movingapi.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class InventoryRequest {

    @NotNull
    private UUID moveId;

    @NotNull
    private UUID itemId;

    @NotNull
    @Min(1)
    private Integer quantity;

    private Boolean isFragile;

    private String customerNotes;
}

