package com.lycheetech.movingapi.rfq.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class RfqRequest {

    @NotNull
    private UUID userId;

    private UUID serviceId;

    @NotNull
    private UUID statusId;

    @NotBlank
    private String originAddress;

    @NotBlank
    private String destinationAddress;

    @NotNull
    @Future
    private LocalDate moveDate;

    private String description;
    private String itemDetails;
}

