package com.lycheetech.movingapi.lookup.movingservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovingServiceResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private boolean active;
}

