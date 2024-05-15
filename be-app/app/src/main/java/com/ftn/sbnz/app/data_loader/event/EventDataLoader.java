package com.ftn.sbnz.app.data_loader.event;

import com.ftn.sbnz.app.core.user.organizer.service.OrganizerService;
import com.ftn.sbnz.app.core.user.visitor.service.VisitorService;
import com.ftn.sbnz.app.data_loader.user.UserDataConstants;
import com.ftn.sbnz.app.feature.event.service.EventService;
import com.ftn.sbnz.model.event.EventEntity;
import com.ftn.sbnz.model.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.ftn.sbnz.app.data_loader.event.EventConstants.FINISHED_EVENT_ID;
import static com.ftn.sbnz.app.data_loader.event.EventConstants.PENDING_EVENT_ID;
import static com.ftn.sbnz.app.data_loader.user.UserDataConstants.ORGANIZER_EMAIL;

@RequiredArgsConstructor
@Component
public class EventDataLoader {

    private final VisitorService visitorService;
    private final OrganizerService organizerService;
    private final EventService eventService;

    public void load()  {
        eventService.save(buildFinishedEvent());
        eventService.save(buildPendingEvent());

        IntStream.range(0, 5).forEach(i -> {
            eventService.save(buildRandomPendingEvent(i));
            eventService.save(buildRandomFinishedEvent(i));
        });
    }

    private EventEntity buildFinishedEvent() {
        var organizer = organizerService.findByEmail(ORGANIZER_EMAIL).orElseThrow(() -> new RuntimeException("Organizer not found"));
        var visitor = visitorService.findByEmail(UserDataConstants.VISITOR_EMAIL).orElseThrow(() -> new RuntimeException("Visitor not found"));
        return EventEntity.builder()
                .id(FINISHED_EVENT_ID)
                .name("Finished event")
                .startDateTime(LocalDateTime.now().minusDays(2))
                .endDateTime(LocalDateTime.now().minusDays(1))
                .totalSeats(100)
                .numberOfAvailableSeats(99)
                .price(200)
                .shortDescription("Short description")
                .detailedDescription("Detailed description")
                .organizationPlan("Organization plan")
                .type(EventType.CONCERT)
                .organizer(organizer)
                .visitors(List.of(visitor))
                .build();
    }

    private EventEntity buildPendingEvent() {
        var organizer = organizerService.findByEmail(ORGANIZER_EMAIL).orElseThrow(() -> new RuntimeException("Organizer not found"));
        var visitor = visitorService.findByEmail(UserDataConstants.VISITOR_EMAIL).orElseThrow(() -> new RuntimeException("Visitor not found"));

        return EventEntity.builder()
                .id(PENDING_EVENT_ID)
                .name("Pending event")
                .startDateTime(LocalDateTime.now().plusDays(10))
                .endDateTime(LocalDateTime.now().plusDays(11))
                .totalSeats(100)
                .numberOfAvailableSeats(100)
                .price(100)
                .shortDescription("Short description")
                .detailedDescription("Detailed description")
                .organizationPlan("Organization plan")
                .organizer(organizer)
                .visitors(List.of())
                .type(EventType.FOOTBALL_MATCH)
                .build();
    }

    private EventEntity buildRandomPendingEvent(int i) {
        var organizer = organizerService.findByEmail(ORGANIZER_EMAIL).orElseThrow(() -> new RuntimeException("Organizer not found"));
        var visitor = visitorService.findByEmail(UserDataConstants.VISITOR_EMAIL).orElseThrow(() -> new RuntimeException("Visitor not found"));

        return EventEntity.builder()
                .id(UUID.randomUUID())
                .name("Random pending event " + i)
                .startDateTime(LocalDateTime.now().plusDays(10))
                .endDateTime(LocalDateTime.now().plusDays(11))
                .totalSeats(100)
                .numberOfAvailableSeats(99)
                .price(100)
                .shortDescription("Short description")
                .detailedDescription("Detailed description")
                .organizationPlan("Organization plan")
                .organizer(organizer)
                .visitors(List.of())
                .type(EventType.SPA_TREATMENT)
                .build();
    }

    private EventEntity buildRandomFinishedEvent(int i) {
        var organizer = organizerService.findByEmail(ORGANIZER_EMAIL).orElseThrow(() -> new RuntimeException("Organizer not found"));
        var visitor = visitorService.findByEmail(UserDataConstants.VISITOR_EMAIL).orElseThrow(() -> new RuntimeException("Visitor not found"));

        return EventEntity.builder()
                .id(UUID.randomUUID())
                .name("Random finished event " + i)
                .startDateTime(LocalDateTime.now().minusDays(2))
                .endDateTime(LocalDateTime.now().minusDays(1))
                .totalSeats(100)
                .numberOfAvailableSeats(99)
                .price(200)
                .shortDescription("Short description")
                .detailedDescription("Detailed description")
                .organizationPlan("Organization plan")
                .organizer(organizer)
                .visitors(List.of(visitor))
                .type(EventType.CYCLING)
                .build();
    }
}
