package com.ftn.sbnz.app.data_loader.event;

import com.ftn.sbnz.app.core.country.db.CountryRepository;
import com.ftn.sbnz.app.core.user.organizer.service.OrganizerService;
import com.ftn.sbnz.app.core.user.visitor.service.VisitorService;
import com.ftn.sbnz.app.data_loader.user.UserDataConstants;
import com.ftn.sbnz.app.feature.event.service.EventService;
import com.ftn.sbnz.app.feature.event.service.SpecialOfferService;
import com.ftn.sbnz.model.event.EventEntity;
import com.ftn.sbnz.model.event.EventType;
import com.ftn.sbnz.model.event.SpecialOfferEntity;
import com.ftn.sbnz.model.event.SpecialOfferType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.ftn.sbnz.app.data_loader.country.CountryDataConstants.COUNTRY_ID_1;
import static com.ftn.sbnz.app.data_loader.country.CountryDataConstants.COUNTRY_ID_2;
import static com.ftn.sbnz.app.data_loader.event.EventConstants.*;
import static com.ftn.sbnz.app.data_loader.user.UserDataConstants.ORGANIZER_EMAIL;

@RequiredArgsConstructor
@Component
public class EventDataLoader {

    private final VisitorService visitorService;
    private final OrganizerService organizerService;
    private final EventService eventService;
    private final CountryRepository countryRepository;
    private final SpecialOfferService specialOfferService;

    private final Random random = new Random();

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
        SpecialOfferEntity specialOffer = specialOfferService.save(buildSpecialOffer());
        eventService.save(buildFinishedEvent());
        eventService.save(buildPendingEvent());
        eventService.save(buildFilledCapacityEvent());
        eventService.save(buildEventWithSpecialOffer(specialOffer));

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
        var country = countryRepository.findById(COUNTRY_ID_2).orElseThrow(() -> new RuntimeException("Country not found!"));

        return EventEntity.builder()
                .id(PENDING_EVENT_ID)
                .name("Pending event")
                .startDateTime(LocalDateTime.now().plusDays(1))
                .endDateTime(LocalDateTime.now().plusDays(2))
                .totalSeats(2000)
                .numberOfAvailableSeats(2000)
                .price(1000)
                .shortDescription("Short description")
                .detailedDescription("Detailed description")
                .organizationPlan("Organization plan")
                .organizer(organizer)
                .visitors(List.of())
                .type(EventType.FOOTBALL_MATCH)
                .country(country)
                .build();
    }

    private EventEntity buildFilledCapacityEvent() {
        var organizer = organizerService.findByEmail(ORGANIZER_EMAIL).orElseThrow(() -> new RuntimeException("Organizer not found"));
        var visitor = visitorService.findByEmail(UserDataConstants.VISITOR_EMAIL).orElseThrow(() -> new RuntimeException("Visitor not found"));
        var country = countryRepository.findById(COUNTRY_ID_2).orElseThrow(() -> new RuntimeException("Country not found!"));

        return EventEntity.builder()
                .id(FILLED_CAPACITY_EVENT_ID)
                .name("Filled capacity event")
                .startDateTime(LocalDateTime.now().plusDays(20))
                .endDateTime(LocalDateTime.now().plusDays(21))
                .totalSeats(1)
                .numberOfAvailableSeats(0)
                .price(1000)
                .shortDescription("Short description")
                .detailedDescription("Detailed description")
                .organizationPlan("Organization plan")
                .organizer(organizer)
                .visitors(List.of(visitor))
                .type(EventType.HIKING)
                .country(country)
                .build();
    }

    private EventEntity buildRandomPendingEvent(int i) {
        var organizer = organizerService.findByEmail(ORGANIZER_EMAIL).orElseThrow(() -> new RuntimeException("Organizer not found"));

        var countryId = (i % 2 == 0) ? COUNTRY_ID_1 : COUNTRY_ID_2;
        var country = countryRepository.findById(countryId).orElseThrow(() -> new RuntimeException("Country not found!"));

        EventType type = EventType.SPA_TREATMENT;
        if (i >= EVENT_COUNT) {
            type = types[i - EVENT_COUNT];
        }

        int visitorCapacity = random.nextInt(50, 1500);
        return EventEntity.builder()
                .id(UUID.randomUUID())
                .name("Random pending event " + i)
                .startDateTime(LocalDateTime.now().plusDays(10))
                .endDateTime(LocalDateTime.now().plusDays(11))
                .totalSeats(visitorCapacity)
                .numberOfAvailableSeats(visitorCapacity)
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

    private SpecialOfferEntity buildSpecialOffer() {
        return SpecialOfferEntity.builder()
                .discount(0.33)
                .type(SpecialOfferType.FOR_LOCALS)
                .build();
    }
    private EventEntity buildEventWithSpecialOffer(SpecialOfferEntity specialOffer) {
        var organizer = organizerService.findByEmail(ORGANIZER_EMAIL).orElseThrow(() -> new RuntimeException("Organizer not found"));
        var country = countryRepository.findById(COUNTRY_ID_1).orElseThrow(() -> new RuntimeException("Country not found!"));

        return EventEntity.builder()
                .id(EVENT_WITH_SPECIAL_OFFER_ID)
                .name("Event with special offer")
                .startDateTime(LocalDateTime.now().plusDays(20))
                .endDateTime(LocalDateTime.now().plusDays(21))
                .totalSeats(20)
                .numberOfAvailableSeats(20)
                .price(200)
                .shortDescription("Short description")
                .detailedDescription("Detailed description")
                .organizationPlan("Organization plan")
                .organizer(organizer)
                .visitors(List.of())
                .specialOffer(specialOffer)
                .type(EventType.BASKETBALL_GAME)
                .country(country)
                .build();
    }
}
