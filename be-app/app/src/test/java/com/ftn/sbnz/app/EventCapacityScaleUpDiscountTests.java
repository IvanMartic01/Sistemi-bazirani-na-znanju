package com.ftn.sbnz.app;

import com.ftn.sbnz.app.feature.event.service.impl.DefaultEventService;
import com.ftn.sbnz.model.core.CountryEntity;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import com.ftn.sbnz.model.event.EventEntity;
import com.ftn.sbnz.model.event.EventPurchaseEntity;
import com.ftn.sbnz.model.event.EventPurchaseStatus;
import com.ftn.sbnz.model.event.EventType;
import com.ftn.sbnz.model.event.pojo.EventScaleUpPrice;
import org.drools.template.ObjectDataCompiler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.ftn.sbnz.app.core.drools.KnowledgeSessionHelper.createKieSessionFromDRL;
import static com.ftn.sbnz.app.data_loader.country.CountryDataConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EventCapacityScaleUpDiscountTests {

    private static KieSession kSession;

    private static final int NUMBER_OF_SEATS = 1000;

    // rule 1
    private static final double RULE1_CAPACITY_MIN = 0.0;
    private static final double RULE1_CAPACITY_MAX = 0.7;
    private static final double RULE1_FILLED_CAPACITY = 0.1;
    private static final double RULE1_INCREASE = 0.0;

    // rule 2
    private static final double RULE2_CAPACITY_MIN = 0.7;
    private static final double RULE2_CAPACITY_MAX = 0.8;
    private static final double RULE2_FILLED_CAPACITY = 0.7;
    private static final double RULE2_INCREASE = 0.1;

    // rule 3
    private static final double RULE3_CAPACITY_MIN = 0.8;
    private static final double RULE3_CAPACITY_MAX = 0.9;
    private static final double RULE3_FILLED_CAPACITY = 0.8;
    private static final double RULE3_INCREASE = 0.2;

    // rule 4
    private static final double RULE4_CAPACITY_MIN = 0.9;
    private static final double RULE4_CAPACITY_MAX = 1.0;
    private static final double RULE4_FILLED_CAPACITY = 0.9;
    private static final double RULE4_INCREASE = 0.3;

    private static EventEntity event;
    private static EventPurchaseEntity purchase;
    private static VisitorEntity visitor;

    @BeforeAll
    public static void setup() {
        visitor = VisitorEntity.builder()
                .id(UUID.randomUUID())
                .build();
        event = EventEntity.builder()
                .id(UUID.randomUUID())
                .name("Pending event")
                .totalSeats(NUMBER_OF_SEATS)
                .numberOfAvailableSeats(NUMBER_OF_SEATS)
                .startDateTime(LocalDateTime.now().plusDays(2))
                .endDateTime(LocalDateTime.now().plusDays(3))
                .price(1000.0)
                .shortDescription("Short description")
                .detailedDescription("Detailed description")
                .organizationPlan("Organization plan")
                .organizer(OrganizerEntity.builder().id(UUID.randomUUID()).build())
                .visitors(List.of(visitor))
                .type(EventType.HIKING)
                .country(CountryEntity.builder()
                        .id(COUNTRY_ID_1)
                        .name(COUNTRY_NAME_1)
                        .city(COUNTRY_CITY_1)
                        .build())
                .build();
        purchase = EventPurchaseEntity.builder()
                .id(UUID.randomUUID())
                .visitor(visitor)
                .event(event)
                .status(EventPurchaseStatus.PAID)
                .purchasePrice(event.getPrice())
                .purchaseTime(new Date(System.currentTimeMillis()))
                .build();

        InputStream templateStream = DefaultEventService.class.getResourceAsStream("/template/event_capacity_scale_up_price.drt");
        List<EventScaleUpPrice> data = new ArrayList<>();

        data.add(new EventScaleUpPrice(RULE1_CAPACITY_MIN, RULE1_CAPACITY_MAX, RULE1_INCREASE));
        data.add(new EventScaleUpPrice(RULE2_CAPACITY_MIN, RULE2_CAPACITY_MAX, RULE2_INCREASE));
        data.add(new EventScaleUpPrice(RULE3_CAPACITY_MIN, RULE3_CAPACITY_MAX, RULE3_INCREASE));
        data.add(new EventScaleUpPrice(RULE4_CAPACITY_MIN, RULE4_CAPACITY_MAX, RULE4_INCREASE));

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(data, templateStream);

        kSession = createKieSessionFromDRL(drl);
    }

    @AfterAll
    public static void cleanup() {
        kSession.destroy();
    }

    @BeforeEach
    public void refresh() {
        purchase.setPurchasePrice(event.getPrice());
        for (FactHandle factHandle : kSession.getFactHandles()) {
            kSession.delete(factHandle);
        }
    }

    @Test
    public void shouldNotGetIncrease_70PercentOrLessCapacity() {
        event.setNumberOfAvailableSeats(event.getTotalSeats() - (int) (event.getTotalSeats() * RULE1_FILLED_CAPACITY));
        kSession.insert(event);
        kSession.insert(purchase);
        kSession.insert(visitor);
        kSession.fireAllRules();

        assertEquals(event.getPrice() * (1.0 + RULE1_INCREASE), purchase.getPurchasePrice());
    }

    @Test
    public void shouldGet10PercentIncrease_70To80PercentCapacity() {
        event.setNumberOfAvailableSeats(event.getTotalSeats() - (int) (event.getTotalSeats() * RULE2_FILLED_CAPACITY));
        kSession.insert(event);
        kSession.insert(purchase);
        kSession.insert(visitor);
        kSession.fireAllRules();

        assertEquals(event.getPrice() * (1.0 + RULE2_INCREASE), purchase.getPurchasePrice());
    }

    @Test
    public void shouldGet20PercentIncrease_80To90PercentCapacity() {
        event.setNumberOfAvailableSeats(event.getTotalSeats() - (int) (event.getTotalSeats() * RULE3_FILLED_CAPACITY));
        kSession.insert(event);
        kSession.insert(purchase);
        kSession.insert(visitor);
        kSession.fireAllRules();

        assertEquals(event.getPrice() * (1.0 + RULE3_INCREASE), purchase.getPurchasePrice());
    }

    @Test
    public void shouldGet30PercentIncrease_90To100PercentCapacity() {
        event.setNumberOfAvailableSeats(event.getTotalSeats() - (int) (event.getTotalSeats() * RULE4_FILLED_CAPACITY));
        kSession.insert(event);
        kSession.insert(purchase);
        kSession.insert(visitor);
        kSession.fireAllRules();

        assertEquals(event.getPrice() * (1.0 + RULE4_INCREASE), purchase.getPurchasePrice());
    }

}
