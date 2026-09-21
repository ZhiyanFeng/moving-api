package com.lycheetech.movingapi.rfq.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RfqRequest {

    private Long serviceId;

    private Long statusId;

    @NotNull
    @Valid
    private RfqAddress originAddress;

    @NotNull
    @Valid
    private RfqAddress destinationAddress;

    @NotNull
    @Future
    private LocalDate moveDate;

    private String notes;

    @Valid
    private List<SpecialtyItem> specialtyItems;
}

