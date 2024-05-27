package com.ftn.sbnz.model.event;

import com.ftn.sbnz.model.core.OrganizerEntity;
import jakarta.persistence.*;
import lombok.*;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Role(Role.Type.EVENT)
@Timestamp("alterDate")
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

    private Date alterDate;

    @ManyToOne
    private EventEntity event;

    @ManyToOne
    private OrganizerEntity organizer;

}
