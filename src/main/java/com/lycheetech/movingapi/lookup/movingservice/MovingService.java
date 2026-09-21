package com.lycheetech.movingapi.lookup.movingservice;

import com.lycheetech.movingapi.common.audit.TimestampedEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "moving_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovingService extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;
}

