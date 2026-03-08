package com.lycheetech.movingapi.lookup.businessservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BusinessServiceResponse {

    private UUID id;
    private String name;
    private String description;
}

