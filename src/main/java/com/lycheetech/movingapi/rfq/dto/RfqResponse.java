package com.lycheetech.movingapi.rfq.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RfqResponse {
    private UUID id;
    private UUID userId;
    private String userName;
    private UUID serviceId;
    private String serviceName;
    private String status;
    private String originAddress;
    private String destinationAddress;
    private LocalDate moveDate;
    private String description;
    private String itemDetails;
    private LocalDateTime createdAt;
}

