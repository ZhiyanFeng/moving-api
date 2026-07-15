package com.lycheetech.movingapi.move.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MoveResponse {
    private UUID id;
    private UUID rfqId;
    private UUID moverId;
    private String moverName;
    private UUID customerId;
    private UUID bidId;
    private UUID vehicleId;
    private String status;
    private LocalDate startDate;
    private LocalDate completionDate;
    private String notes;
    private LocalDateTime createdAt;
}

