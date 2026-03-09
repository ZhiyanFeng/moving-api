package com.lycheetech.movingapi.lookup.movingservice;

import com.lycheetech.movingapi.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "moving_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovingService extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}

