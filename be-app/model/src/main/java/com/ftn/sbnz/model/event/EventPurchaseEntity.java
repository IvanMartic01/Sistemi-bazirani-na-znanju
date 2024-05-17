package com.ftn.sbnz.model.event;

import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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
}
