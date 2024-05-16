package com.ftn.sbnz.app.data_loader.event;

import com.ftn.sbnz.app.core.country.db.CountryRepository;
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

import static com.ftn.sbnz.app.data_loader.country.CountryDataConstants.COUNTRY_ID_1;
import static com.ftn.sbnz.app.data_loader.country.CountryDataConstants.COUNTRY_ID_2;
import static com.ftn.sbnz.app.data_loader.event.EventConstants.FINISHED_EVENT_ID;
import static com.ftn.sbnz.app.data_loader.event.EventConstants.PENDING_EVENT_ID;
import static com.ftn.sbnz.app.data_loader.user.UserDataConstants.ORGANIZER_EMAIL;

@RequiredArgsConstructor
@Component
public class EventDataLoader {

    private final VisitorService visitorService;
    private final OrganizerService organizerService;
    private final EventService eventService;
    private final CountryRepository countryRepository;

    private final EventType[] types = new EventType[] {
            EventType.ART_LECTURE,
            EventType.ART_WORKSHOP,
            EventType.PARAGLIDING,
            EventType.BALLOON_RIDE,
            EventType.MICHELIN_STAR_RESTAURANT,
            EventType.MULTIPLE_GENRE_CONCERT
    };
    private final int EVENT_COUNT = 5;

    public void load()  {
        eventService.save(buildFinishedEvent());
        eventService.save(buildPendingEvent());

        IntStream.range(0, EVENT_COUNT + types.length).forEach(i -> {
            eventService.save(buildRandomPendingEvent(i));
            eventService.save(buildRandomFinishedEvent(i));
        });
    }

    private EventEntity buildFinishedEvent() {
        var organizer = organizerService.findByEmail(ORGANIZER_EMAIL).orElseThrow(() -> new RuntimeException("Organizer not found"));
        var visitor = visitorService.findByEmail(UserDataConstants.VISITOR_EMAIL).orElseThrow(() -> new RuntimeException("Visitor not found"));
        var country = countryRepository.findById(COUNTRY_ID_1).orElseThrow(() -> new RuntimeException("Country not found!"));

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
                .country(country)
                .build();
    }

    private EventEntity buildPendingEvent() {
        var organizer = organizerService.findByEmail(ORGANIZER_EMAIL).orElseThrow(() -> new RuntimeException("Organizer not found"));
        var visitor = visitorService.findByEmail(UserDataConstants.VISITOR_EMAIL).orElseThrow(() -> new RuntimeException("Visitor not found"));
        var country = countryRepository.findById(COUNTRY_ID_2).orElseThrow(() -> new RuntimeException("Country not found!"));

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
                .country(country)
                .build();
    }

    private EventEntity buildRandomPendingEvent(int i) {
        var organizer = organizerService.findByEmail(ORGANIZER_EMAIL).orElseThrow(() -> new RuntimeException("Organizer not found"));
        var visitor = visitorService.findByEmail(UserDataConstants.VISITOR_EMAIL).orElseThrow(() -> new RuntimeException("Visitor not found"));

        var countryId = (i % 2 == 0) ? COUNTRY_ID_1 : COUNTRY_ID_2;
        var country = countryRepository.findById(countryId).orElseThrow(() -> new RuntimeException("Country not found!"));

        EventType type = EventType.SPA_TREATMENT;
        if (i >= EVENT_COUNT) {
            type = types[i - EVENT_COUNT];
        }

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
                .type(type)
                .country(country)
                .build();
    }

    private EventEntity buildRandomFinishedEvent(int i) {
        var organizer = organizerService.findByEmail(ORGANIZER_EMAIL).orElseThrow(() -> new RuntimeException("Organizer not found"));
        var visitor = visitorService.findByEmail(UserDataConstants.VISITOR_EMAIL).orElseThrow(() -> new RuntimeException("Visitor not found"));
        var countryId = (i % 2 == 0) ? COUNTRY_ID_1 : COUNTRY_ID_2;
        var country = countryRepository.findById(countryId).orElseThrow(() -> new RuntimeException("Country not found!"));

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
                .country(country)
                .build();
    }
}
