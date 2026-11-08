package com.lycheetech.movingapi.lookup.geo;

import com.lycheetech.movingapi.common.audit.TimestampedEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subdivisions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subdivision extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(nullable = false, length = 10)
    private String code;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}

