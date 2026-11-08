package com.lycheetech.movingapi.rfq.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class RfqResponse {
    private UUID id;
    private UUID userId;
    private String userName;
    private Long serviceId;
    private String serviceName;
    private String status;
    private RfqAddress originAddress;
    private RfqAddress destinationAddress;
    private LocalDate moveDate;
    private String notes;
    private List<SpecialtyItem> specialtyItems;
    // Estimated driving distance (km) between origin and destination, via
    // Google Distance Matrix API; null if unavailable.
    private BigDecimal estimatedDistanceKm;
    private LocalDateTime createdAt;
}

