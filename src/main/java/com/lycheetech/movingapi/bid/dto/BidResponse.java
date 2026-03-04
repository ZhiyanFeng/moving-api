package com.lycheetech.movingapi.bid.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BidResponse {
    private UUID id;
    private UUID moverId;
    private String moverName;
    private UUID rfqId;
    private String status;
    private BigDecimal amount;
    private String details;
    private LocalDateTime createdAt;
}

