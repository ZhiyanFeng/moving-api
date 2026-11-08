package com.lycheetech.movingapi.bid;

import com.lycheetech.movingapi.common.audit.BaseEntity;
import com.lycheetech.movingapi.mover.Mover;
import com.lycheetech.movingapi.rfq.Rfq;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "bids")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mover_id", nullable = false)
    private Mover mover;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rfq_id", nullable = false)
    private Rfq rfq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private BidStatus status;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "pricing_type", nullable = false, columnDefinition = "bid_pricing_type")
    private PricingType pricingType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Only meaningful when pricingType = HOURLY; DB CHECK constraint enforces
    // it stays 0 for FIXED bids.
    @Column(name = "min_hours")
    @Builder.Default
    private Integer minHours = 0;

    private String details;
}

