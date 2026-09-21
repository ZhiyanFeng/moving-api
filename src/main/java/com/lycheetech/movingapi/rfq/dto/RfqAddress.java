package com.lycheetech.movingapi.rfq.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Structured snapshot of an address, stored as JSONB on the rfqs table
 * (origin_address / destination_address). Reused as-is for the request
 * payload, the persisted entity attribute, and the response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfqAddress {

    @NotBlank
    private String formattedAddress;

    private String placeId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String unitApartmentNumber;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
}

