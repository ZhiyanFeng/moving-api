package com.lycheetech.movingapi.mover;

import com.lycheetech.movingapi.common.audit.BaseEntity;
import com.lycheetech.movingapi.lookup.geo.SubArea;
import com.lycheetech.movingapi.lookup.movingservice.MovingService;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "movers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Soft delete: a mover with bid/move history must not be hard-deleted.
@SQLDelete(sql = "UPDATE movers SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Mover extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "company_name")
    private String companyName;

    @Column(nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;
    private String description;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "vehicle_url")
    private String vehicleUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "mover_service_areas",
            joinColumns = @JoinColumn(name = "mover_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_area_id")
    )
    @Builder.Default
    private Set<SubArea> serviceAreas = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "mover_services",
            joinColumns = @JoinColumn(name = "mover_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @Builder.Default
    private Set<MovingService> services = new HashSet<>();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}

