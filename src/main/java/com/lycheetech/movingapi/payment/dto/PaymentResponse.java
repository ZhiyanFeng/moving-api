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
    private UUID moveId;
    private String stripeStatus;
    private String stripePaymentIntentId;
    private String stripeInvoiceId;
    private String stripeReceiptUrl;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

