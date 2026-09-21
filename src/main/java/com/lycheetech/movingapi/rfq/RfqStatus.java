package com.lycheetech.movingapi.rfq;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rfq_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RfqStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}

