package com.lycheetech.movingapi.lookup.movingservice;

import com.lycheetech.movingapi.common.audit.TimestampedEntity;
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
public class MovingService extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}

