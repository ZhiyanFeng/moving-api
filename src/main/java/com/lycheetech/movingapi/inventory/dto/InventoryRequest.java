package com.lycheetech.movingapi.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class InventoryRequest {

    @NotNull
    private UUID bookingId;

    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    private Integer quantity;

    private String description;
}

