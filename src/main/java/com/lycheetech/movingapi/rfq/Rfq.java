package com.lycheetech.movingapi.rfq;

import com.lycheetech.movingapi.common.audit.BaseEntity;
import com.lycheetech.movingapi.lookup.movingservice.MovingService;
import com.lycheetech.movingapi.rfq.dto.RfqAddress;
import com.lycheetech.movingapi.rfq.dto.SpecialtyItem;
import com.lycheetech.movingapi.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rfqs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rfq extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private MovingService service;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private RfqStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "origin_address", nullable = false, columnDefinition = "jsonb")
    private RfqAddress originAddress;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "destination_address", nullable = false, columnDefinition = "jsonb")
    private RfqAddress destinationAddress;

    @Column(name = "move_date", nullable = false)
    private LocalDate moveDate;

    private String notes;

    // Ad-hoc items needing special handling (e.g. piano, pool table), captured
    // as free-form JSON since there is no inventory catalog table backing this.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "specialty_items", nullable = false, columnDefinition = "jsonb")
    @Builder.Default
    private List<SpecialtyItem> specialtyItems = new ArrayList<>();
}

