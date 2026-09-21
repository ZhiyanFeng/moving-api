package com.lycheetech.movingapi.move;

import com.lycheetech.movingapi.common.audit.TimestampedEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_statuses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoveStatus extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}

