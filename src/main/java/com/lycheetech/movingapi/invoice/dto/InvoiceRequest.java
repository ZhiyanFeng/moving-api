package com.lycheetech.movingapi.invoice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class InvoiceRequest {

    @NotNull
    private UUID bookingId;

    private UUID paymentId;

    @NotNull
    private UUID statusId;

    @NotNull
    @Positive
    private BigDecimal amountDue;

    @NotNull
    private LocalDate issueDate;

    @NotNull
    private LocalDate dueDate;
}

