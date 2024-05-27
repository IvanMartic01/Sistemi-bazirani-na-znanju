package com.ftn.sbnz.model.event;

import com.ftn.sbnz.model.core.CountryEntity;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EventEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Column(nullable = false)
    private int totalSeats;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String shortDescription;

    @Column(nullable = false)
    private String detailedDescription;

    @Column(nullable = false)
    private String organizationPlan;

    @ManyToOne
    @JoinColumn(name = "organizer_id", referencedColumnName = "id")
    private OrganizerEntity organizer;

    @ManyToMany
    @JoinTable(
            name = "event_visitor",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "visitor_id"))
    private Collection<VisitorEntity> visitors = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    private int numberOfAvailableSeats;

    @ManyToOne
    private CountryEntity country;

    @ManyToOne
    private SpecialOfferEntity specialOffer;

}
