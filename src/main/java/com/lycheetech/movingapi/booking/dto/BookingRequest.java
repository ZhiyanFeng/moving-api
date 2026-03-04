package com.lycheetech.movingapi.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingRequest {

    @NotNull
    private UUID rfqId;

    @NotNull
    private UUID moverId;

    @NotNull
    private UUID bidId;

    @NotNull
    private UUID vehicleId;

    @NotNull
    private UUID statusId;

    @NotNull
    private LocalDate startDate;

    private LocalDate completionDate;
    private String notes;
}

