package com.lycheetech.movingapi.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentRequest {

    @NotNull
    private UUID moveId;

    private String stripeStatus;

    private String stripePaymentIntentId;

    private String stripeInvoiceId;

    private String stripeReceiptUrl;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String currency;
}

