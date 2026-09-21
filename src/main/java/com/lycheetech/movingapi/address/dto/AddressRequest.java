package com.lycheetech.movingapi.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AddressRequest {

    private UUID userId;

    // Google Maps Identifiers
    @NotBlank(message = "Google place id is required")
    private String googlePlaceId;

    @NotBlank(message = "Formatted address is required")
    private String formattedAddress;

    // Coordinates for instant mapping
    @NotNull(message = "Latitude is required")
    private BigDecimal latitude;

    @NotNull(message = "Longitude is required")
    private BigDecimal longitude;

    // Parsing for specific business logic (taxing, dispatching)
    private String postalCode;
    private String city;
    private String stateProvince;
    private String country;

    // Custom Logistics (Google doesn't provide this)
    private String label;
    private String unitApartmentNumber;
    private Boolean hasElevator = false;
    private String parkingRestrictions;
}

