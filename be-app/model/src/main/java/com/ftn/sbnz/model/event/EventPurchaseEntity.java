package com.ftn.sbnz.model.event;

import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import jakarta.persistence.*;
import lombok.*;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Role(Role.Type.EVENT)
@Timestamp("purchaseTime")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class EventPurchaseEntity {

    @Id
    private UUID id;

    @ManyToOne
    private VisitorEntity visitor;

    @ManyToOne
    private EventEntity event;

    @Enumerated(EnumType.STRING)
    private EventPurchaseStatus status;

    private Double purchasePrice;

    @Column(nullable = false)
    private Date purchaseTime;

    @Column(nullable = true)
    private LocalDateTime cancellationTime;
}
