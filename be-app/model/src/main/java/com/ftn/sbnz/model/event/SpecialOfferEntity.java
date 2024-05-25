package com.ftn.sbnz.model.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SpecialOfferEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private double discount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SpecialOfferType type;
}
