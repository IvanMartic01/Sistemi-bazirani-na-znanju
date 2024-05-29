package com.ftn.sbnz.app;

import com.ftn.sbnz.app.feature.event.service.impl.DefaultEventService;
import com.ftn.sbnz.model.core.CountryEntity;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import com.ftn.sbnz.model.drools_helper.PrecipitationType;
import com.ftn.sbnz.model.drools_helper.WeatherBroadcast;
import com.ftn.sbnz.model.drools_helper.template_object.SeasonalDiscount;
import com.ftn.sbnz.model.event.EventEntity;
import com.ftn.sbnz.model.event.EventPurchaseEntity;
import com.ftn.sbnz.model.event.EventPurchaseStatus;
import com.ftn.sbnz.model.event.EventType;
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
public class SeasonalDiscountTests {

    private static KieSession kSession;

    // rule 1 constants
    private static final double RULE1_TEMPERATURE = 30.0;
    private static final PrecipitationType RULE1_PRECIPITATION = PrecipitationType.NOTHING;
    private static final double RULE1_DISCOUNT = 0.15;

    // rule 2 constants
    private static final PrecipitationType RULE2_PRECIPITATION1 = PrecipitationType.NOTHING;
    private static final PrecipitationType RULE2_PRECIPITATION2 = PrecipitationType.RAIN;
    private static final double RULE2_DISCOUNT = 0.20;

    private static EventEntity event;
    private static EventPurchaseEntity purchase;

    @BeforeAll
    public static void setup() {
        event = EventEntity.builder()
                .id(UUID.randomUUID())
                .name("Pending event")
                .totalSeats(2000)
                .numberOfAvailableSeats(2000)
                .startDateTime(LocalDateTime.now().plusDays(2))
                .endDateTime(LocalDateTime.now().plusDays(3))
                .price(1000.0)
                .shortDescription("Short description")
                .detailedDescription("Detailed description")
                .organizationPlan("Organization plan")
                .organizer(OrganizerEntity.builder().id(UUID.randomUUID()).build())
                .visitors(List.of())
                .type(EventType.HIKING)
                .country(CountryEntity.builder()
                                .id(COUNTRY_ID_1)
                                .name(COUNTRY_NAME_1)
                                .city(COUNTRY_CITY_1)
                                .build())
                .build();
        purchase = EventPurchaseEntity.builder()
                .id(UUID.randomUUID())
                .visitor(VisitorEntity.builder().id(UUID.randomUUID()).build())
                .event(event)
                .status(EventPurchaseStatus.PAID)
                .purchasePrice(event.getPrice())
                .purchaseTime(new Date(System.currentTimeMillis()))
                .build();

        InputStream templateStream = DefaultEventService.class.getResourceAsStream("/template/event_seasonal_discount.drt");

        List<SeasonalDiscount> data = new ArrayList<>();

        // rule 1: outdoor events
        data.add(new SeasonalDiscount(EventType.HIKING.name(), Double.MAX_VALUE, RULE1_TEMPERATURE, RULE1_PRECIPITATION.name(), RULE1_DISCOUNT));
        data.add(new SeasonalDiscount(EventType.CYCLING.name(), Double.MAX_VALUE, RULE1_TEMPERATURE, RULE1_PRECIPITATION.name(), RULE1_DISCOUNT));
        data.add(new SeasonalDiscount(EventType.PICNIC.name(), Double.MAX_VALUE, RULE1_TEMPERATURE, RULE1_PRECIPITATION.name(), RULE1_DISCOUNT));
        data.add(new SeasonalDiscount(EventType.ZOO_VISIT.name(), Double.MAX_VALUE, RULE1_TEMPERATURE, RULE1_PRECIPITATION.name(), RULE1_DISCOUNT));
        data.add(new SeasonalDiscount(EventType.THEME_PARK_VISIT.name(), Double.MAX_VALUE, RULE1_TEMPERATURE, RULE1_PRECIPITATION.name(), RULE1_DISCOUNT));
        data.add(new SeasonalDiscount(EventType.FOOTBALL_MATCH.name(), Double.MAX_VALUE, RULE1_TEMPERATURE, RULE1_PRECIPITATION.name(), RULE1_DISCOUNT));
        data.add(new SeasonalDiscount(EventType.PARAGLIDING.name(), Double.MAX_VALUE, RULE1_TEMPERATURE, RULE1_PRECIPITATION.name(), RULE1_DISCOUNT));
        data.add(new SeasonalDiscount(EventType.BALLOON_RIDE.name(), Double.MAX_VALUE, RULE1_TEMPERATURE, RULE1_PRECIPITATION.name(), RULE1_DISCOUNT));

        // rule 2: no snow
        data.add(new SeasonalDiscount(EventType.WINTER_FESTIVAL.name(), Double.MAX_VALUE, Double.MIN_VALUE, RULE2_PRECIPITATION1.name(), RULE2_DISCOUNT));
        data.add(new SeasonalDiscount(EventType.WINTER_FESTIVAL.name(), Double.MAX_VALUE, Double.MIN_VALUE, RULE2_PRECIPITATION2.name(), RULE2_DISCOUNT));

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
        event.setType(EventType.HIKING);
        purchase.setPurchasePrice(event.getPrice());
        for (FactHandle factHandle : kSession.getFactHandles()) {
            kSession.delete(factHandle);
        }
    }

    @Test
    public void rule1_shouldApplyDiscount_OutdoorTemperatureHigh() {
        WeatherBroadcast weatherBroadcast = WeatherBroadcast.builder()
                .temperature(RULE1_TEMPERATURE + 5.0)
                .precipitation(RULE1_PRECIPITATION)
                .date(event.getStartDateTime().toLocalDate())
                .build();

        kSession.insert(weatherBroadcast);
        kSession.insert(purchase);

        kSession.fireAllRules();

        assertEquals((1.0 - RULE1_DISCOUNT) * event.getPrice(), purchase.getPurchasePrice());
    }

    @Test
    public void rule1_shouldNotApplyDiscount_OutdoorTemperatureLow() {
        WeatherBroadcast weatherBroadcast = WeatherBroadcast.builder()
                .temperature(RULE1_TEMPERATURE - 5.0)
                .precipitation(RULE1_PRECIPITATION)
                .date(event.getStartDateTime().toLocalDate())
                .build();

        kSession.insert(weatherBroadcast);
        kSession.insert(purchase);

        kSession.fireAllRules();

        assertEquals(event.getPrice(), purchase.getPurchasePrice());
    }

    @Test
    public void rule1_shouldNotApplyDiscount_OutdoorNotSunny() {
        WeatherBroadcast weatherBroadcast = WeatherBroadcast.builder()
                .temperature(RULE1_TEMPERATURE + 5.0)
                .precipitation(PrecipitationType.RAIN)
                .date(event.getStartDateTime().toLocalDate())
                .build();

        kSession.insert(weatherBroadcast);
        kSession.insert(purchase);

        kSession.fireAllRules();

        assertEquals(event.getPrice(), purchase.getPurchasePrice());
    }

    @Test
    public void rule1_shouldNotApplyDiscount_NotOutdoorActivity() {
        event.setType(EventType.GALLERY_VISIT);
        WeatherBroadcast weatherBroadcast = WeatherBroadcast.builder()
                .temperature(RULE1_TEMPERATURE + 5.0)
                .precipitation(RULE1_PRECIPITATION)
                .date(event.getStartDateTime().toLocalDate())
                .build();

        kSession.insert(weatherBroadcast);
        kSession.insert(purchase);

        kSession.fireAllRules();

        assertEquals(event.getPrice(), purchase.getPurchasePrice());
    }

    @Test
    public void rule2_shouldApplyDiscount_WinterFestivalNoSnow() {
        event.setType(EventType.WINTER_FESTIVAL);
        WeatherBroadcast weatherBroadcast = WeatherBroadcast.builder()
                .temperature(10.0)
                .precipitation(RULE2_PRECIPITATION1)
                .date(event.getStartDateTime().toLocalDate())
                .build();

        kSession.insert(weatherBroadcast);
        kSession.insert(purchase);

        kSession.fireAllRules();

        assertEquals((1.0 - RULE2_DISCOUNT) * event.getPrice(), purchase.getPurchasePrice());
    }

    @Test
    public void rule2_shouldNotApplyDiscount_NotWinterFestival() {
        event.setType(EventType.HIKING);
        WeatherBroadcast weatherBroadcast = WeatherBroadcast.builder()
                .temperature(10.0)
                .precipitation(RULE2_PRECIPITATION1)
                .date(event.getStartDateTime().toLocalDate())
                .build();

        kSession.insert(weatherBroadcast);
        kSession.insert(purchase);

        kSession.fireAllRules();

        assertEquals(event.getPrice(), purchase.getPurchasePrice());
    }

    @Test
    public void rule2_shouldNotApplyDiscount_WinterFestivalSnowing() {
        event.setType(EventType.WINTER_FESTIVAL);
        WeatherBroadcast weatherBroadcast = WeatherBroadcast.builder()
                .temperature(10.0)
                .precipitation(PrecipitationType.SNOW)
                .date(event.getStartDateTime().toLocalDate())
                .build();

        kSession.insert(weatherBroadcast);
        kSession.insert(purchase);

        kSession.fireAllRules();

        assertEquals(event.getPrice(), purchase.getPurchasePrice());
    }

}
