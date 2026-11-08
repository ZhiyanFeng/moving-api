package com.lycheetech.movingapi.lookup.geo;

import com.lycheetech.movingapi.common.audit.TimestampedEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Country extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 2)
    private String code;

    @Column(nullable = false, unique = true)
    private String name;

    // Deprecation flag: retire a region from active use without breaking
    // historical FK references (e.g. addresses/mover_service_areas).
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}

