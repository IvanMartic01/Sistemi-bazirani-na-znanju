package com.ftn.sbnz.service.feature.event.model;

import com.ftn.sbnz.service.core.user.organizer.db.OrganizerEntity;
import com.ftn.sbnz.service.core.user.visitor.db.VisitorEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


@Getter
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

    private int numberOfAvailableSeats;
}
