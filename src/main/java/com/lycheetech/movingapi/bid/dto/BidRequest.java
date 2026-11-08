package com.lycheetech.movingapi.bid.dto;

import com.lycheetech.movingapi.bid.PricingType;
import jakarta.validation.constraints.Min;
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
    private Long statusId;

    @NotNull
    private PricingType pricingType;

    @NotNull
    @Positive
    private BigDecimal price;

    @Min(0)
    private Integer minHours;

    private String details;
}

