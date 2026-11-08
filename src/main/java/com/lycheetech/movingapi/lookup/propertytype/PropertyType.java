package com.lycheetech.movingapi.lookup.propertytype;

import com.lycheetech.movingapi.common.audit.TimestampedEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents the fixed catalog of home/property categories
 * (e.g. '3-bedroom-house') used to estimate move size and pricing.
 */
@Entity
@Table(name = "property_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyType extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String category;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    @Column(name = "room_count", nullable = false)
    @Builder.Default
    private Integer roomCount = 0;

    @Column(name = "is_basement", nullable = false)
    @Builder.Default
    private boolean isBasement = false;

    @Column(name = "estimated_cubic_feet", nullable = false)
    private Integer estimatedCubicFeet;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;
}

