package com.lycheetech.movingapi.lookup.geo;

import com.lycheetech.movingapi.common.audit.TimestampedEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sub_areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubArea extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "metro_area_id", nullable = false)
    private MetroArea metroArea;

    @Column(nullable = false)
    private String name;

    private String description;
}

