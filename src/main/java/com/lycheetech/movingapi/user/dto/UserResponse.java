package com.lycheetech.movingapi.user.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID id;
    private String firebaseUid;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String avatarUrl;
    private BigDecimal ratingAvg;
    private Integer completedMoves;
    private Integer canceledMoves;
    private OffsetDateTime createdAt;
}

