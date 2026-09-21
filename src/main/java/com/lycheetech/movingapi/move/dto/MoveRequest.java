package com.lycheetech.movingapi.move.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MoveRequest {

    @NotNull
    private UUID rfqId;

    @NotNull
    private UUID moverId;

    @NotNull
    private UUID customerId;

    @NotNull
    private UUID bidId;


    @NotNull
    private Long statusId;

    // Operational Links (Points to the addresses table)
    private UUID pickupAddressId;
    private UUID dropoffAddressId;

    // Historical Snapshots (Locked in at time of billing)
    @NotNull
    private String finalPickupAddressText;

    @NotNull
    private String finalDropoffAddressText;

    private Integer billedDistanceMeters;
    private BigDecimal finalPrice;

    @NotNull
    private LocalDateTime scheduledDate;

    private String notes;
}

