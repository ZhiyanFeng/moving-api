package com.lycheetech.movingapi.invoice.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class InvoiceResponse {
    private UUID id;
    private UUID bookingId;
    private UUID paymentId;
    private String status;
    private BigDecimal amountDue;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
}

