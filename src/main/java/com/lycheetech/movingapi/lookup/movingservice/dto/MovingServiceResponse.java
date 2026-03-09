package com.lycheetech.movingapi.lookup.movingservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MovingServiceResponse {

    private UUID id;
    private String name;
    private String description;
}

