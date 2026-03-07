package com.lycheetech.movingapi.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PaymentResponse {
    private UUID id;
    private UUID bookingId;
    private String status;
    private BigDecimal amount;
    private String paymentType;
    private String transactionId;
    private LocalDateTime paymentDate; // maps to BaseEntity.createdAt
}

