package com.lycheetech.movingapi.move.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
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
    private String status;
    private UUID pickupAddressId;
    private UUID dropoffAddressId;
    private String finalPickupAddressText;
    private String finalDropoffAddressText;
    private Integer billedDistanceMeters;
    private BigDecimal finalPrice;
    private LocalDateTime scheduledDate;
    private String notes;
    private LocalDateTime createdAt;
}

