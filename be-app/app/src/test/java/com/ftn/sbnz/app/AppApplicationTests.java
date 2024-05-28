package com.ftn.sbnz.app;

import com.ftn.sbnz.app.core.drools.KnowledgeSessionHelper;
import com.ftn.sbnz.model.core.CountryEntity;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.core.Role;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import com.ftn.sbnz.model.event.*;
import lombok.RequiredArgsConstructor;
import org.drools.core.time.SessionPseudoClock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.ftn.sbnz.app.data_loader.country.CountryDataConstants.*;
import static com.ftn.sbnz.app.data_loader.user.UserDataConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RequiredArgsConstructor
@SpringBootTest
class AppApplicationTests {

    private static EventEntity pendingEvent;
    private static OrganizerEntity organizer;
    private static CountryEntity country;

    @BeforeAll
    public static void setup() {
        organizer = OrganizerEntity.builder()
                .id(ORGANIZER_ID)
                .email(ORGANIZER_EMAIL)
                .password("Password")
                .role(Role.ORGANIZER)
                .name(ORGANIZER_NAME)
                .enabled(true)
                .build();

        country = CountryEntity.builder()
                .id(COUNTRY_ID_1)
                .name(COUNTRY_NAME_1)
                .city(COUNTRY_CITY_1)
                .build();

        UUID event1UUID = UUID.randomUUID();
        pendingEvent = EventEntity.builder()
                .id(event1UUID)
                .name("Pending event")
                .totalSeats(2000)
                .numberOfAvailableSeats(2000)
                .startDateTime(LocalDateTime.now().plusDays(2))
                .endDateTime(LocalDateTime.now().plusDays(3))
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

    @Test
    public void fiveChangesInLastHour() {
        KieContainer kieContainer = KnowledgeSessionHelper.createRuleBase();
        KieSession kSession = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "cep-test-session");

        Collection<EventEntity> eventsToPromote = new HashSet<>();
        Collection<EventEntity> eventsToDelete = new HashSet<>();
        Collection<EventPurchaseEntity> purchasesToDelete = new HashSet<>();

        kSession.setGlobal("eventsToPromote", eventsToPromote);
        kSession.setGlobal("eventsToDelete", eventsToDelete);
        kSession.setGlobal("purchasesToDelete", purchasesToDelete);

        SessionPseudoClock clock = kSession.getSessionClock();
        clock.advanceTime(0, TimeUnit.MINUTES);

        Collection<EventEntity> events = new ArrayList<>();
        Collection<EventPurchaseEntity> purchases = new ArrayList<>();
        Collection<EventAlterationLogEntity> eventAlterLogs = new ArrayList<>();

        final int ALTERATIONS = 5;
        IntStream.range(0, ALTERATIONS).forEach(i -> {
            clock.advanceTime(i * 5L, TimeUnit.MINUTES);
            Date currentTime = new Date(clock.getCurrentTime());
            eventAlterLogs.add(EventAlterationLogEntity.builder()
                    .organizer(organizer)
                    .alterDate(currentTime)
                    .event(pendingEvent)
                    .build());
        });

        kSession.insert(pendingEvent);
        eventAlterLogs.forEach(kSession::insert);

        kSession.setGlobal("now", LocalDateTime.now());
        kSession.fireAllRules();

        kSession.destroy();

        assertEquals(1, eventsToDelete.size());

    }

    @Test
    public void bought50PercentOfTicketsLastHour() {
        KieContainer kieContainer = KnowledgeSessionHelper.createRuleBase();
        KieSession kSession = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "cep-test-session");

        Collection<EventEntity> eventsToPromote = new HashSet<>();
        Collection<EventEntity> eventsToDelete = new HashSet<>();
        Collection<EventPurchaseEntity> purchasesToDelete = new HashSet<>();

        kSession.setGlobal("eventsToPromote", eventsToPromote);
        kSession.setGlobal("eventsToDelete", eventsToDelete);
        kSession.setGlobal("purchasesToDelete", purchasesToDelete);

        SessionPseudoClock clock = kSession.getSessionClock();
        clock.advanceTime(0, TimeUnit.MINUTES);

        Collection<EventEntity> events = new ArrayList<>();
        Collection<EventPurchaseEntity> purchases = new ArrayList<>();
        Collection<EventAlterationLogEntity> eventAlterLogs = new ArrayList<>();

        final double TICKETS_SOLD = pendingEvent.getTotalSeats() * 0.8;
        final double TICKETS_SOLD_BEFORE_LAST_HOUR = TICKETS_SOLD * 0.3;
        final double TICKETS_SOLD_LAST_HOUR = TICKETS_SOLD * 0.7;
        IntStream.range(0, (int) TICKETS_SOLD_BEFORE_LAST_HOUR).forEach(i -> {
            Date currentTime = new Date(clock.getCurrentTime());
            EventPurchaseEntity purchase = EventPurchaseEntity.builder()
                    .id(UUID.randomUUID())
                    .visitor(VisitorEntity.builder().id(UUID.randomUUID()).build())
                    .event(pendingEvent)
                    .status(EventPurchaseStatus.PAID)
                    .purchasePrice(10000000.0)
                    .purchaseTime(currentTime)
                    .build();
            purchases.add(purchase);
        });
        clock.advanceTime(3, TimeUnit.HOURS);
        IntStream.range(0, (int) TICKETS_SOLD_LAST_HOUR).forEach(i -> {
            Date currentTime = new Date(clock.getCurrentTime());
            EventPurchaseEntity purchase = EventPurchaseEntity.builder()
                    .id(UUID.randomUUID())
                    .visitor(VisitorEntity.builder().id(UUID.randomUUID()).build())
                    .event(pendingEvent)
                    .status(EventPurchaseStatus.PAID)
                    .purchasePrice(10000000.0)
                    .purchaseTime(currentTime)
                    .build();
            purchases.add(purchase);
        });

        kSession.insert(pendingEvent);
        purchases.forEach(kSession::insert);

        kSession.setGlobal("now", LocalDateTime.now());
        kSession.fireAllRules();

        kSession.destroy();

        assertEquals(1, eventsToDelete.size());
    }

    @Test
    public void cancelled20PercentOfTickets() {
        KieContainer kieContainer = KnowledgeSessionHelper.createRuleBase();
        KieSession kSession = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "cep-test-session");

        Collection<EventEntity> eventsToPromote = new HashSet<>();
        Collection<EventEntity> eventsToDelete = new HashSet<>();
        Collection<EventPurchaseEntity> purchasesToDelete = new HashSet<>();

        kSession.setGlobal("eventsToPromote", eventsToPromote);
        kSession.setGlobal("eventsToDelete", eventsToDelete);
        kSession.setGlobal("purchasesToDelete", purchasesToDelete);

        SessionPseudoClock clock = kSession.getSessionClock();
        clock.advanceTime(0, TimeUnit.MINUTES);

        Collection<EventEntity> events = new ArrayList<>();
        Collection<EventPurchaseEntity> purchases = new ArrayList<>();
        Collection<EventAlterationLogEntity> eventAlterLogs = new ArrayList<>();

        final double TICKETS_SOLD = pendingEvent.getTotalSeats() * 0.9;
        final double TICKETS_PAID = TICKETS_SOLD * 0.75;
        final double TICKETS_CANCELLED = TICKETS_SOLD * 0.25;
        IntStream.range(0, (int) TICKETS_PAID).forEach(i -> {
            Date currentTime = new Date(clock.getCurrentTime());
            EventPurchaseEntity purchase = EventPurchaseEntity.builder()
                    .id(UUID.randomUUID())
                    .visitor(VisitorEntity.builder().id(UUID.randomUUID()).build())
                    .event(pendingEvent)
                    .status(EventPurchaseStatus.PAID)
                    .purchasePrice(10000000.0)
                    .purchaseTime(currentTime)
                    .build();
            purchases.add(purchase);
        });
        clock.advanceTime(3, TimeUnit.HOURS);
        IntStream.range(0, (int) TICKETS_CANCELLED).forEach(i -> {
            Date currentTime = new Date(clock.getCurrentTime());
            EventPurchaseEntity purchase = EventPurchaseEntity.builder()
                    .id(UUID.randomUUID())
                    .visitor(VisitorEntity.builder().id(UUID.randomUUID()).build())
                    .event(pendingEvent)
                    .status(EventPurchaseStatus.CANCELLED)
                    .cancellationTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(clock.getCurrentTime()), ZoneOffset.UTC))
                    .purchasePrice(10000000.0)
                    .purchaseTime(currentTime)
                    .build();
            purchases.add(purchase);
        });

        kSession.insert(pendingEvent);
        purchases.forEach(kSession::insert);

        kSession.setGlobal("now", LocalDateTime.now());
        kSession.fireAllRules();

        kSession.destroy();

        assertEquals(1, eventsToDelete.size());
    }


}
