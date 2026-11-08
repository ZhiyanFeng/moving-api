package com.lycheetech.movingapi.address;

import com.lycheetech.movingapi.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Soft delete: historical moves reference addresses via pickup/dropoff FKs,
// so the row must survive even after a user "removes" it from their address book.
@SQLDelete(sql = "UPDATE addresses SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Google Maps Identifiers
    @Column(name = "google_place_id", nullable = false)
    private String googlePlaceId;

    @Column(name = "formatted_address", nullable = false)
    private String formattedAddress;

    // Coordinates for instant mapping
    @Column(nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    // Parsing for specific business logic (taxing, dispatching)
    @Column(name = "postal_code")
    private String postalCode;

    private String city;

    @Column(name = "state_province")
    private String stateProvince;

    private String country;

    // Custom Logistics (Google doesn't provide this)
    private String label;

    @Column(name = "unit_apartment_number")
    private String unitApartmentNumber;

    @Column(name = "has_elevator", nullable = false)
    @Builder.Default
    private Boolean hasElevator = false;

    @Column(name = "parking_restrictions")
    private String parkingRestrictions;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

