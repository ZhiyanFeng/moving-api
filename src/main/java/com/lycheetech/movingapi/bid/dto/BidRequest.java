package com.lycheetech.movingapi.bid.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BidRequest {

    @NotNull
    private UUID moverId;

    @NotNull
    private UUID rfqId;

    @NotNull
    private UUID statusId;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String details;
}

