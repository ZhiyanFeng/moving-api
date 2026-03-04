package com.lycheetech.movingapi.mover.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class MoverRequest {

    @NotBlank
    private String name;

    private String companyName;

    @NotBlank
    @Email
    private String email;

    private String phoneNumber;
    private String address;
    private String description;
    private String avatarUrl;
    private String vehicleUrl;
    private Set<UUID> regionIds;
    private Set<UUID> serviceIds;
}

