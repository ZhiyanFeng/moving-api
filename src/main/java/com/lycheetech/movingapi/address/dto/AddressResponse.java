package com.lycheetech.movingapi.address.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AddressResponse {

    private UUID id;
    private UUID userId;

    private String googlePlaceId;
    private String formattedAddress;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private String postalCode;
    private String city;
    private String stateProvince;
    private String country;

    private String label;
    private String unitApartmentNumber;
    private Boolean hasElevator;
    private String parkingRestrictions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

