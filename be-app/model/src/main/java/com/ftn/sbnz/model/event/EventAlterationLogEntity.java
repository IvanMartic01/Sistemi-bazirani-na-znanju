package com.ftn.sbnz.model.event;

import com.ftn.sbnz.model.core.OrganizerEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
public class EventAlterationLogEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private LocalDateTime alterDate;

    @ManyToOne
    private EventEntity event;

    @ManyToOne
    private OrganizerEntity organizer;

}
