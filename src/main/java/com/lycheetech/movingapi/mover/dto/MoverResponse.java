package com.lycheetech.movingapi.mover.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class MoverResponse {
    private UUID id;
    private String name;
    private String companyName;
    private String email;
    private String phoneNumber;
    private String address;
    private String description;
    private String avatarUrl;
    private String vehicleUrl;
    private Set<String> regions;
    private Set<String> services;
    private LocalDateTime createdAt;
}

