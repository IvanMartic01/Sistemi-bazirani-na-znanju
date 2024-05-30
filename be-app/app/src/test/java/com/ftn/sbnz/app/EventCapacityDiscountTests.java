package com.ftn.sbnz.app;

import com.ftn.sbnz.app.feature.event.service.impl.DefaultEventService;
import com.ftn.sbnz.model.core.CountryEntity;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import com.ftn.sbnz.model.event.EventEntity;
import com.ftn.sbnz.model.event.EventPurchaseEntity;
import com.ftn.sbnz.model.event.EventPurchaseStatus;
import com.ftn.sbnz.model.event.EventType;
import com.ftn.sbnz.model.event.pojo.EventCapacityDiscount;
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
public class EventCapacityDiscountTests {

    private static KieSession kSession;

    // rule 1
    private static final int RULE1_CAPACITY_MIN = 0;
    private static final int RULE1_CAPACITY_MAX = 100;
    private static final double RULE1_DISCOUNT = 0.0;
    private static final int RULE1_SEATS = 50;

    // rule 2
    private static final int RULE2_CAPACITY_MIN = 100;
    private static final int RULE2_CAPACITY_MAX = 1000;
    private static final double RULE2_DISCOUNT = 0.1;
    private static final int RULE2_SEATS = 500;

    // rule 3
    private static final int RULE3_CAPACITY_MIN = 1000;
    private static final int RULE3_CAPACITY_MAX = Integer.MAX_VALUE;
    private static final double RULE3_DISCOUNT = 0.2;
    private static final int RULE3_SEATS = 5000;

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
                .totalSeats(1000)
                .numberOfAvailableSeats(1000)
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

        InputStream templateStream = DefaultEventService.class.getResourceAsStream("/template/event_capacity_discount.drt");

        List<EventCapacityDiscount> data = new ArrayList<>();
        data.add(new EventCapacityDiscount(RULE1_CAPACITY_MIN, RULE1_CAPACITY_MAX, RULE1_DISCOUNT));
        data.add(new EventCapacityDiscount(RULE2_CAPACITY_MIN, RULE2_CAPACITY_MAX, RULE2_DISCOUNT));
        data.add(new EventCapacityDiscount(RULE3_CAPACITY_MIN, RULE3_CAPACITY_MAX, RULE3_DISCOUNT));

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
    public void shouldNotGetDiscount_lessThan100Capacity() {
        event.setTotalSeats(RULE1_SEATS);
        event.setNumberOfAvailableSeats(RULE1_SEATS);
        kSession.insert(event);
        kSession.insert(purchase);
        kSession.insert(visitor);
        kSession.fireAllRules();

        assertEquals(event.getPrice() * (1.0 - RULE1_DISCOUNT), purchase.getPurchasePrice());
    }

    @Test
    public void shouldGet10PercentDiscount_between100And1000Capacity() {
        event.setTotalSeats(RULE2_SEATS);
        event.setNumberOfAvailableSeats(RULE2_SEATS);
        kSession.insert(event);
        kSession.insert(purchase);
        kSession.insert(visitor);
        kSession.fireAllRules();

        assertEquals(event.getPrice() * (1.0 - RULE2_DISCOUNT), purchase.getPurchasePrice());
    }

    @Test
    public void shouldGet20PercentDiscount_greaterThan1000Capacity() {
        event.setTotalSeats(RULE3_SEATS);
        event.setNumberOfAvailableSeats(RULE3_SEATS);
        kSession.insert(event);
        kSession.insert(purchase);
        kSession.insert(visitor);
        kSession.fireAllRules();

        assertEquals(event.getPrice() * (1.0 - RULE3_DISCOUNT), purchase.getPurchasePrice());

    }
}
