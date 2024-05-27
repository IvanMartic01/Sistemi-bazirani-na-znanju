package com.ftn.sbnz.app;

import com.ftn.sbnz.app.core.drools.KnowledgeSessionHelper;
import com.ftn.sbnz.model.core.CountryEntity;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.core.Role;
import com.ftn.sbnz.model.event.EventAlterationLogEntity;
import com.ftn.sbnz.model.event.EventEntity;
import com.ftn.sbnz.model.event.EventPurchaseEntity;
import com.ftn.sbnz.model.event.EventType;
import lombok.RequiredArgsConstructor;
import org.drools.core.time.SessionPseudoClock;
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

        OrganizerEntity organizer1 = OrganizerEntity.builder()
                .id(ORGANIZER_ID)
                .email(ORGANIZER_EMAIL)
                .password("Password")
                .role(Role.ORGANIZER)
                .name(ORGANIZER_NAME)
                .enabled(true)
                .build();

        CountryEntity country1 = CountryEntity.builder()
                .id(COUNTRY_ID_1)
                .name(COUNTRY_NAME_1)
                .city(COUNTRY_CITY_1)
                .build();

        UUID event1UUID = UUID.randomUUID();
        EventEntity event1 = EventEntity.builder()
                .id(event1UUID)
                .name("Pending event")
                .totalSeats(2000)
                .numberOfAvailableSeats(2000)
                .price(1000)
                .shortDescription("Short description")
                .detailedDescription("Detailed description")
                .organizationPlan("Organization plan")
                .organizer(organizer1)
                .visitors(List.of())
                .type(EventType.FOOTBALL_MATCH)
                .country(country1)
                .build();

        SessionPseudoClock clock = kSession.getSessionClock();
        clock.advanceTime(0, TimeUnit.MINUTES);

        Collection<EventEntity> events = new ArrayList<>();
        Collection<EventPurchaseEntity> purchases = new ArrayList<>();
        Collection<EventAlterationLogEntity> eventAlterLogs = new ArrayList<>();

        IntStream.range(0, 10).forEach(i -> {
            clock.advanceTime(i * 5L, TimeUnit.MINUTES);
//            LocalDateTime currentTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(clock.getCurrentTime()), ZoneOffset.UTC);
            Date currentTime = new Date(clock.getCurrentTime());
            eventAlterLogs.add(EventAlterationLogEntity.builder()
                    .organizer(organizer1)
                    .alterDate(currentTime)
                    .event(event1)
                    .build());
        });

        clock.advanceTime(10, TimeUnit.MINUTES);
        LocalDateTime currentTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(clock.getCurrentTime()), ZoneOffset.UTC);
        event1.setStartDateTime(currentTime);
        clock.advanceTime(10, TimeUnit.MINUTES);
        currentTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(clock.getCurrentTime()), ZoneOffset.UTC);
        event1.setEndDateTime(currentTime);
        kSession.insert(event1);
        eventAlterLogs.forEach(kSession::insert);

        kSession.setGlobal("now", currentTime);
        kSession.destroy();

        assertEquals(1, eventsToDelete.size());

    }


}
