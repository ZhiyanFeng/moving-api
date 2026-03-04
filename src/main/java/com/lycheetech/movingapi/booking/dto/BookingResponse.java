package com.lycheetech.movingapi.booking.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BookingResponse {
    private UUID id;
    private UUID rfqId;
    private UUID moverId;
    private String moverName;
    private UUID bidId;
    private UUID vehicleId;
    private String status;
    private LocalDate startDate;
    private LocalDate completionDate;
    private String notes;
    private LocalDateTime createdAt;
}

