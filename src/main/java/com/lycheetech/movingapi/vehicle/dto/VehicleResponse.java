package com.lycheetech.movingapi.vehicle.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class VehicleResponse {
    private UUID id;
    private UUID moverId;
    private String moverName;
    private String status;
    private String make;
    private String model;
    private Integer year;
    private String licensePlate;
    private BigDecimal capacity;
    private LocalDateTime createdAt;
}

