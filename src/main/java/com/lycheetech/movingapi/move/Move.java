package com.lycheetech.movingapi.move;

import com.lycheetech.movingapi.address.Address;
import com.lycheetech.movingapi.bid.Bid;
import com.lycheetech.movingapi.common.audit.BaseEntity;
import com.lycheetech.movingapi.mover.Mover;
import com.lycheetech.movingapi.rfq.Rfq;
import com.lycheetech.movingapi.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "moves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Move extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rfq_id", nullable = false)
    private Rfq rfq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mover_id", nullable = false)
    private Mover mover;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bid_id", nullable = false)
    private Bid bid;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private MoveStatus status;

    // Operational Links (Points to the addresses table)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_address_id")
    private Address pickupAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dropoff_address_id")
    private Address dropoffAddress;

    // Historical Snapshots (Locked in at time of billing)
    @Column(name = "final_pickup_address_text", nullable = false)
    private String finalPickupAddressText;

    @Column(name = "final_dropoff_address_text", nullable = false)
    private String finalDropoffAddressText;

    @Column(name = "billed_distance_meters")
    private Integer billedDistanceMeters;

    @Column(name = "final_price", precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    private String notes;
}

