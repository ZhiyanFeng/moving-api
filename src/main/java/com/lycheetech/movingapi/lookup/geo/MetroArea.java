package com.lycheetech.movingapi.lookup.geo;

import com.lycheetech.movingapi.common.audit.TimestampedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "metro_areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetroArea extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subdivision_id", nullable = false)
    private Subdivision subdivision;

    @Column(nullable = false)
    private String name;

    private String description;
}

