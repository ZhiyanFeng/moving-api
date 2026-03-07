package com.lycheetech.movingapi.mover;

import com.lycheetech.movingapi.common.audit.BaseEntity;
import com.lycheetech.movingapi.lookup.region.Region;
import com.lycheetech.movingapi.lookup.businessservice.BusinessService;
import jakarta.persistence.*;
import lombok.*;

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
public class Mover extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "company_name")
    private String companyName;

    @Column(nullable = false, unique = true)
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
            name = "mover_regions",
            joinColumns = @JoinColumn(name = "mover_id"),
            inverseJoinColumns = @JoinColumn(name = "region_id")
    )
    @Builder.Default
    private Set<Region> regions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "mover_services",
            joinColumns = @JoinColumn(name = "mover_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @Builder.Default
    private Set<BusinessService> services = new HashSet<>();
}

