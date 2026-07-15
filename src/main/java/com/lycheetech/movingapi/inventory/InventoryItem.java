package com.lycheetech.movingapi.inventory;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Master catalog of inventory item types (pre-populated lookup),
 * e.g. 'King Bed', '3-Seater Sofa', 'Medium Box'.
 */
@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "item_name", nullable = false, unique = true)
    private String itemName;

    @Column(nullable = false)
    private String category;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "cubic_feet_volume", nullable = false, precision = 5, scale = 2)
    private BigDecimal cubicFeetVolume;

    @Column(name = "estimated_weight_lbs")
    @Builder.Default
    private Integer estimatedWeightLbs = 0;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}

