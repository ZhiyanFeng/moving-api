package com.lycheetech.movingapi.bid;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bid_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}

