package com.lycheetech.movingapi.vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class VehicleRequest {

    @NotNull
    private UUID moverId;

    @NotNull
    private UUID statusId;

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    @NotNull
    private Integer year;

    @NotBlank
    private String licensePlate;

    @NotNull
    private BigDecimal capacity;
}

