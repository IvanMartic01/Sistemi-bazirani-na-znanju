package com.ftn.sbnz.app;

import com.ftn.sbnz.app.core.drools.KnowledgeSessionHelper;
import com.ftn.sbnz.model.core.CountryEntity;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.core.Role;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import com.ftn.sbnz.model.event.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.drools.core.time.SessionPseudoClock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
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
    private static EventEntity finishedEvent1;
    private static EventEntity finishedEvent2;
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

        pendingEvent = EventEntity.builder()
                .id(UUID.randomUUID())
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

        finishedEvent1 = EventEntity.builder()
                .id(UUID.randomUUID())
                .name("Finished event 1")
                .totalSeats(1000)
                .numberOfAvailableSeats(1000)
                .startDateTime(LocalDateTime.now().minusDays(2))
                .endDateTime(LocalDateTime.now().minusDays(1))
                .price(1000)
                .shortDescription("Short description 1")
                .detailedDescription("Detailed description 1")
                .organizationPlan("Organization plan 1")
                .organizer(organizer)
                .visitors(List.of())
                .type(EventType.FOOTBALL_MATCH)
                .country(country)
                .build();

        finishedEvent2 = EventEntity.builder()
                .id(UUID.randomUUID())
                .name("Finished event 2")
                .totalSeats(1000)
                .numberOfAvailableSeats(1000)
                .startDateTime(LocalDateTime.now().minusDays(3))
                .endDateTime(LocalDateTime.now().minusDays(2))
                .price(1000)
                .shortDescription("Short description 2")
                .detailedDescription("Detailed description 2")
                .organizationPlan("Organization plan 2")
                .organizer(organizer)
                .visitors(List.of())
                .type(EventType.FOOTBALL_MATCH)
                .country(country)
                .build();

    }

    @Test
    public void shouldCancel_fiveChangesInLastThreeDays() {
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
    public void shouldCancel_bought50PercentOfTicketsLastHour() {
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

        Collection<EventPurchaseEntity> purchases = new ArrayList<>();

        final double PROPORTION = 0.3;
        final double TICKETS_SOLD = pendingEvent.getTotalSeats() * 0.8;
        final double TICKETS_SOLD_BEFORE_LAST_HOUR = TICKETS_SOLD * PROPORTION;
        final double TICKETS_SOLD_LAST_HOUR = TICKETS_SOLD * (1.0 - PROPORTION);
        IntStream.range(0, (int) TICKETS_SOLD_BEFORE_LAST_HOUR).forEach(i -> {
            Date currentTime = new Date(clock.getCurrentTime());
            EventPurchaseEntity purchase = EventPurchaseEntity.builder()
                    .id(UUID.randomUUID())
                    .visitor(VisitorEntity.builder().id(UUID.randomUUID()).build())
                    .event(pendingEvent)
                    .status(EventPurchaseStatus.PAID)
                    .purchasePrice(pendingEvent.getPrice())
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
                    .purchasePrice(pendingEvent.getPrice())
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
    public void shouldCancel_cancelled20PercentOfTickets() {
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

        Collection<EventPurchaseEntity> purchases = new ArrayList<>();

        final double PROPORTION = 0.75;
        final double TICKETS_SOLD = pendingEvent.getTotalSeats() * 0.9;
        final double TICKETS_PAID = TICKETS_SOLD * PROPORTION;
        final double TICKETS_CANCELLED = TICKETS_SOLD * (1.0 - PROPORTION);
        purchases.addAll(generatePurchases(
                pendingEvent,
                new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID,
                null,
                (int) TICKETS_PAID));
        clock.advanceTime(3, TimeUnit.HOURS);
        purchases.addAll(generatePurchases(
                pendingEvent,
                new Date(clock.getCurrentTime()),
                EventPurchaseStatus.CANCELLED,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(clock.getCurrentTime()), ZoneOffset.UTC),
                (int) TICKETS_CANCELLED));

        kSession.insert(pendingEvent);
        purchases.forEach(kSession::insert);

        kSession.setGlobal("now", LocalDateTime.now());
        kSession.fireAllRules();

        kSession.destroy();

        assertEquals(1, eventsToDelete.size());
    }

    private Collection<EventPurchaseEntity> generatePurchases(EventEntity event,
                                                              Date purchaseTime,
                                                              EventPurchaseStatus status,
                                                              LocalDateTime cancellationTime,
                                                              int range) {
        Collection<EventPurchaseEntity> purchases = new ArrayList<>();
        IntStream.range(0, range).forEach(i -> {
            EventPurchaseEntity purchase = EventPurchaseEntity.builder()
                    .id(UUID.randomUUID())
                    .visitor(VisitorEntity.builder().id(UUID.randomUUID()).build())
                    .event(event)
                    .status(status)
                    .purchasePrice(event.getPrice())
                    .purchaseTime(purchaseTime)
                    .cancellationTime(cancellationTime)
                    .build();
            purchases.add(purchase);
        });

        return purchases;
    }

    @Test
    public void shouldPromote2Events_70PercentLastHourPurchases() {
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

        Collection<EventPurchaseEntity> purchases = new ArrayList<>();

        // finished event 1
        final double PROPORTION_1 = 0.25;
        final double TICKETS_SOLD_1 = finishedEvent1.getTotalSeats() * 0.8;
        final double TICKETS_SOLD_BEFORE_LAST_HOUR_1 = TICKETS_SOLD_1 * PROPORTION_1;
        final double TICKETS_SOLD_LAST_HOUR_1 = TICKETS_SOLD_1 * (1.0 - PROPORTION_1);

        // finished event 2
        final double PROPORTION_2 = 0.1;
        final double TICKETS_SOLD_2 = finishedEvent2.getTotalSeats() * 0.5;
        final double TICKETS_SOLD_BEFORE_LAST_HOUR_2 = TICKETS_SOLD_2 * PROPORTION_2;
        final double TICKETS_SOLD_LAST_HOUR_2 = TICKETS_SOLD_2 * (1.0 - PROPORTION_2);

        // finished event 1
        purchases.addAll(generatePurchases(finishedEvent1, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_BEFORE_LAST_HOUR_1));
        // finished event 2
        purchases.addAll(generatePurchases(finishedEvent2, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_BEFORE_LAST_HOUR_2));
        clock.advanceTime(3, TimeUnit.HOURS);
        // finished event 1
        purchases.addAll(generatePurchases(finishedEvent1, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_LAST_HOUR_1));
        // finished event 2
        purchases.addAll(generatePurchases(finishedEvent2, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_LAST_HOUR_2));

        finishedEvent1.setType(EventType.WINTER_FESTIVAL);
        finishedEvent2.setType(EventType.CONCERT);
        kSession.insert(finishedEvent1);
        kSession.insert(finishedEvent2);
        purchases.forEach(kSession::insert);

        kSession.setGlobal("now", LocalDateTime.now());
        kSession.fireAllRules();

        kSession.destroy();

        assertEquals(2, eventsToPromote.size());
    }

    @Test
    public void shouldPromote1Event_70PercentLastHourPurchases() {
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

        Collection<EventPurchaseEntity> purchases = new ArrayList<>();

        // finished event 1
        final double PROPORTION_1 = 0.25;
        final double TICKETS_SOLD_1 = finishedEvent1.getTotalSeats() * 0.8;
        final double TICKETS_SOLD_BEFORE_LAST_HOUR_1 = TICKETS_SOLD_1 * PROPORTION_1;
        final double TICKETS_SOLD_LAST_HOUR_1 = TICKETS_SOLD_1 * (1.0 - PROPORTION_1);

        // finished event 2
        final double PROPORTION_2 = 0.5;
        final double TICKETS_SOLD_2 = finishedEvent2.getTotalSeats() * 0.5;
        final double TICKETS_SOLD_BEFORE_LAST_HOUR_2 = TICKETS_SOLD_2 * PROPORTION_2;
        final double TICKETS_SOLD_LAST_HOUR_2 = TICKETS_SOLD_2 * (1.0 - PROPORTION_2);

        // finished event 1
        purchases.addAll(generatePurchases(finishedEvent1, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_BEFORE_LAST_HOUR_1));
        // finished event 2
        purchases.addAll(generatePurchases(finishedEvent2, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_BEFORE_LAST_HOUR_2));
        clock.advanceTime(3, TimeUnit.HOURS);
        // finished event 1
        purchases.addAll(generatePurchases(finishedEvent1, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_LAST_HOUR_1));
        // finished event 2
        purchases.addAll(generatePurchases(finishedEvent2, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_LAST_HOUR_2));

        finishedEvent1.setType(EventType.WINTER_FESTIVAL);
        finishedEvent2.setType(EventType.CONCERT);
        kSession.insert(finishedEvent1);
        kSession.insert(finishedEvent2);
        purchases.forEach(kSession::insert);

        kSession.setGlobal("now", LocalDateTime.now());
        kSession.fireAllRules();

        kSession.destroy();

        assertEquals(1, eventsToPromote.size());
    }

    @Test
    public void shouldNotPromoteAnyEvent_70PercentLastHourPurchases() {
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

        Collection<EventPurchaseEntity> purchases = new ArrayList<>();

        // finished event 1
        final double PROPORTION_1 = 0.4;
        final double TICKETS_SOLD_1 = finishedEvent1.getTotalSeats() * 0.8;
        final double TICKETS_SOLD_BEFORE_LAST_HOUR_1 = TICKETS_SOLD_1 * PROPORTION_1;
        final double TICKETS_SOLD_LAST_HOUR_1 = TICKETS_SOLD_1 * (1.0 - PROPORTION_1);

        // finished event 2
        final double PROPORTION_2 = 0.5;
        final double TICKETS_SOLD_2 = finishedEvent2.getTotalSeats() * 0.5;
        final double TICKETS_SOLD_BEFORE_LAST_HOUR_2 = TICKETS_SOLD_2 * PROPORTION_2;
        final double TICKETS_SOLD_LAST_HOUR_2 = TICKETS_SOLD_2 * (1.0 - PROPORTION_2);

        // finished event 1
        purchases.addAll(generatePurchases(finishedEvent1, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_BEFORE_LAST_HOUR_1));
        // finished event 2
        purchases.addAll(generatePurchases(finishedEvent2, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_BEFORE_LAST_HOUR_2));
        clock.advanceTime(3, TimeUnit.HOURS);
        // finished event 1
        purchases.addAll(generatePurchases(finishedEvent1, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_LAST_HOUR_1));
        // finished event 2
        purchases.addAll(generatePurchases(finishedEvent2, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_SOLD_LAST_HOUR_2));

        finishedEvent1.setType(EventType.WINTER_FESTIVAL);
        finishedEvent2.setType(EventType.CONCERT);
        kSession.insert(finishedEvent1);
        kSession.insert(finishedEvent2);
        purchases.forEach(kSession::insert);

        kSession.setGlobal("now", LocalDateTime.now());
        kSession.fireAllRules();

        kSession.destroy();

        assertEquals(0, eventsToPromote.size());
    }

    @Test
    public void shouldPromote1Event_10PercentCancellation() {
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

        Collection<EventPurchaseEntity> purchases = new ArrayList<>();

        // finished event 1
        final double PROPORTION_1 = 0.9;
        final double TICKETS_SOLD_1 = finishedEvent1.getTotalSeats() * 0.8;
        final double TICKETS_PAID_1 = TICKETS_SOLD_1 * PROPORTION_1;
        final double TICKETS_CANCELLED_1 = TICKETS_SOLD_1 * (1.0 - PROPORTION_1);

        // finished event 2
        final double PROPORTION_2 = 0.8;
        final double TICKETS_SOLD_2 = finishedEvent2.getTotalSeats() * 0.5;
        final double TICKETS_PAID_2 = TICKETS_SOLD_2 * PROPORTION_2;
        final double TICKETS_CANCELLED_2 = TICKETS_SOLD_2 * (1.0 - PROPORTION_2);

        // finished event 1
        purchases.addAll(generatePurchases(finishedEvent1, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_PAID_1));
        // finished event 2
        purchases.addAll(generatePurchases(finishedEvent2, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_PAID_2));
        // finished event 1
        purchases.addAll(generatePurchases(finishedEvent1, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.CANCELLED,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(clock.getCurrentTime()), ZoneOffset.UTC),
                (int) TICKETS_CANCELLED_1));
        // finished event 2
        purchases.addAll(generatePurchases(finishedEvent2, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.CANCELLED,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(clock.getCurrentTime()), ZoneOffset.UTC),
                (int) TICKETS_CANCELLED_2));

        finishedEvent1.setType(EventType.WINTER_FESTIVAL);
        finishedEvent2.setType(EventType.CONCERT);
        kSession.insert(finishedEvent1);
        kSession.insert(finishedEvent2);
        purchases.forEach(kSession::insert);

        kSession.setGlobal("now", LocalDateTime.now());
        kSession.fireAllRules();

        kSession.destroy();

        assertEquals(1, eventsToPromote.size());
    }

    @Test
    public void shouldNotPromote_10PercentCancellation() {
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

        Collection<EventPurchaseEntity> purchases = new ArrayList<>();

        // finished event 1
        final double PROPORTION_1 = 0.7;
        final double TICKETS_SOLD_1 = finishedEvent1.getTotalSeats() * 0.8;
        final double TICKETS_PAID_1 = TICKETS_SOLD_1 * PROPORTION_1;
        final double TICKETS_CANCELLED_1 = TICKETS_SOLD_1 * (1.0 - PROPORTION_1);

        // finished event 2
        final double PROPORTION_2 = 0.8;
        final double TICKETS_SOLD_2 = finishedEvent2.getTotalSeats() * 0.5;
        final double TICKETS_PAID_2 = TICKETS_SOLD_2 * PROPORTION_2;
        final double TICKETS_CANCELLED_2 = TICKETS_SOLD_2 * (1.0 - PROPORTION_2);

        // finished event 1
        purchases.addAll(generatePurchases(finishedEvent1, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_PAID_1));
        // finished event 2
        purchases.addAll(generatePurchases(finishedEvent2, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.PAID, null, (int) TICKETS_PAID_2));
        // finished event 1
        purchases.addAll(generatePurchases(finishedEvent1, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.CANCELLED,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(clock.getCurrentTime()), ZoneOffset.UTC),
                (int) TICKETS_CANCELLED_1));
        // finished event 2
        purchases.addAll(generatePurchases(finishedEvent2, new Date(clock.getCurrentTime()),
                EventPurchaseStatus.CANCELLED,
                LocalDateTime.ofInstant(Instant.ofEpochMilli(clock.getCurrentTime()), ZoneOffset.UTC),
                (int) TICKETS_CANCELLED_2));

        finishedEvent1.setType(EventType.WINTER_FESTIVAL);
        finishedEvent2.setType(EventType.CONCERT);
        kSession.insert(finishedEvent1);
        kSession.insert(finishedEvent2);
        purchases.forEach(kSession::insert);

        kSession.setGlobal("now", LocalDateTime.now());
        kSession.fireAllRules();

        kSession.destroy();

        assertEquals(0, eventsToPromote.size());
    }

}
