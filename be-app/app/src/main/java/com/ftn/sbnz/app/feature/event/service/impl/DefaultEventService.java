package com.ftn.sbnz.app.feature.event.service.impl;

import com.ftn.sbnz.app.core.country.service.CountryService;
import com.ftn.sbnz.app.core.drools.KnowledgeSessionHelper;
import com.ftn.sbnz.app.core.other.exception.StartDateIsAfterEndDateException;
import com.ftn.sbnz.app.core.service.MailService;
import com.ftn.sbnz.app.core.user.visitor.service.VisitorService;
import com.ftn.sbnz.app.core.utils.drools_helper.WeatherBroadcastGenerator;
import com.ftn.sbnz.app.feature.auth.service.AuthService;
import com.ftn.sbnz.app.feature.event.dto.CreateUpdateEventRequestDto;
import com.ftn.sbnz.app.feature.event.dto.EventPurchaseDto;
import com.ftn.sbnz.app.feature.event.dto.EventResponseDto;
import com.ftn.sbnz.app.feature.event.exception.EventException;
import com.ftn.sbnz.app.feature.event.exception.EventNotFoundException;
import com.ftn.sbnz.app.feature.event.mapper.EventMapper;
import com.ftn.sbnz.app.feature.event.mapper.EventPurchaseMapper;
import com.ftn.sbnz.app.feature.event.repository.EventAlterationLogRepository;
import com.ftn.sbnz.app.feature.event.repository.EventRepository;
import com.ftn.sbnz.app.feature.event.service.EventAlterationLogService;
import com.ftn.sbnz.app.feature.event.service.EventPurchaseService;
import com.ftn.sbnz.app.feature.event.service.EventService;
import com.ftn.sbnz.app.feature.event.service.SpecialOfferService;
import com.ftn.sbnz.model.core.CountryEntity;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.drools_helper.PrecipitationType;
import com.ftn.sbnz.model.drools_helper.WeatherBroadcast;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import com.ftn.sbnz.model.drools_helper.template_object.SeasonalDiscount;
import com.ftn.sbnz.model.event.*;
import com.ftn.sbnz.model.event.pojo.EventCapacityDiscount;
import com.ftn.sbnz.model.event.pojo.EventScaleUpPrice;
import lombok.RequiredArgsConstructor;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

import static com.ftn.sbnz.app.core.drools.KnowledgeSessionHelper.createKieSessionFromDRL;

@RequiredArgsConstructor
@Service
public class DefaultEventService implements EventService {

    private final EventRepository eventRepository;

    private final EventPurchaseService eventPurchaseService;
    private final VisitorService visitorService;
    private final AuthService authService;
    private final CountryService countryService;
    private final SpecialOfferService specialOfferService;
    private final EventAlterationLogService eventAlterationLogService;
    private final MailService mailService;

    private final EventMapper eventMapper;
    private final EventPurchaseMapper eventPurchaseMapper;

    private final WeatherBroadcastGenerator weatherBroadcastGenerator;

    @Override
    public EventEntity save(EventEntity eventEntity) {
        return eventRepository.save(eventEntity);
    }

    @Override
    public EventResponseDto getEventById(UUID id) {
        EventEntity eventEntity = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        EventEntity clonedEvent = eventEntity.toBuilder().build();

        try {
            VisitorEntity visitor = authService.getVisitorForCurrentSession();
            var eventPurchaseOptional = eventPurchaseService.getEventPurchaseByEventIdAndVisitorId(eventEntity.getId(), visitor.getId());

            if (eventPurchaseOptional.isPresent()) eventEntity.setPrice(eventPurchaseOptional.get().getPurchasePrice());
            else {
                var calculatedPurchase = calculateEventPurchasePrice(clonedEvent, visitor);
                eventEntity.setPrice(calculatedPurchase.getPurchasePrice());
            }
            return eventMapper.toDto(eventEntity);
        }catch (Exception e) {
            return eventMapper.toDto(eventEntity);
        }
    }

    @Override
    public EventResponseDto createEvent(CreateUpdateEventRequestDto dto) {
        OrganizerEntity organizer = authService.getOrganizerForCurrentSession();

        EventEntity eventToBeSaved = eventMapper.toEntity(dto, organizer);
        CountryEntity country = countryService.getEntityById(UUID.fromString(dto.getCountryId()));
        eventToBeSaved.setCountry(country);

        // Adding special offer if there is one
        if (dto.getSpecialOffer() != null) {
            SpecialOfferEntity specialOffer = specialOfferService.saveAndGetEntity(dto.getSpecialOffer());
            eventToBeSaved.setSpecialOffer(specialOffer);
        }

        EventEntity savedEvent = eventRepository.save(eventToBeSaved);
        return eventMapper.toDto(savedEvent);
    }

    @Override
    public EventResponseDto updateEvent(UUID id, CreateUpdateEventRequestDto dto) {
        OrganizerEntity organizer = authService.getOrganizerForCurrentSession();

        EventEntity eventToUpdate = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        validateUpdate(eventToUpdate, organizer, dto);

        EventEntity updatedEvent = updateEvent(eventToUpdate, dto);
        CountryEntity country = countryService.getEntityById(UUID.fromString(dto.getCountryId()));
        updatedEvent.setCountry(country);

        // Updating special offer if there is one
        if (dto.getSpecialOffer() != null) {
            SpecialOfferEntity specialOffer = specialOfferService
                    .updateAndGetEntity(updatedEvent.getSpecialOffer().getId(), dto.getSpecialOffer());
            updatedEvent.setSpecialOffer(specialOffer);
        }

        updatedEvent = eventRepository.save(updatedEvent);
        eventAlterationLogService.saveAndGetEntity(EventAlterationLogEntity.builder()
                .event(updatedEvent)
//                .alterDate(LocalDateTime.now())
                .alterDate(new Date())
                .organizer(organizer)
                .build());
        return eventMapper.toDto(updatedEvent);
    }

    private EventEntity updateEvent(EventEntity eventToUpdate, CreateUpdateEventRequestDto dto) {
        return eventToUpdate.toBuilder()
                .name(dto.getName())
                .startDateTime(dto.getStartDateTime())
                .endDateTime(dto.getEndDateTime())
                .totalSeats(dto.getTotalSeats())
                .price(dto.getPrice())
                .shortDescription(dto.getShortDescription())
                .detailedDescription(dto.getDetailedDescription())
                .organizationPlan(dto.getOrganizationPlan())
                .numberOfAvailableSeats(eventToUpdate.getTotalSeats() - eventToUpdate.getVisitors().size())
                .build();
    }

    private void validateUpdate(EventEntity event, OrganizerEntity organizer, CreateUpdateEventRequestDto dto) {
        if (!hasOrganizerCreatedEvent(event, organizer)) {
            throw new EventException("You can't update event that you didn't create.");
        }
        if (isEventStarted(event)) {
            throw new EventException("You can't update event that already started.");
        }
        if (isEventEnded(event)) {
            throw new EventException("You can't update event that already ended.");
        }

        if (dto.getTotalSeats() > event.getNumberOfAvailableSeats()) {
            throw new EventException("Total seats cannot be less than the number of visitors already registered for the event");
        }

        if (dto.getStartDateTime().isAfter(dto.getEndDateTime())) {
            throw new StartDateIsAfterEndDateException();
        }

        if (dto.getStartDateTime().isBefore(LocalDateTime.now().plusDays(3))) {
            throw new EventException("Event start date must be at least 3 days in the future.");
        }
    }

    @Override
    public void deleteEvent(UUID id) {
        OrganizerEntity organizer = authService.getOrganizerForCurrentSession();

        EventEntity event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        validateDeletion(event, organizer);
        var eventToBeRemoved = event.toBuilder().visitors(List.of()).build();
        eventRepository.delete(eventToBeRemoved);
    }

    private void validateDeletion(EventEntity event, OrganizerEntity organizer) {
        if (!hasOrganizerCreatedEvent(event, organizer)) {
            throw new EventException("You can't delete event that you didn't create.");
        }

        if (isEventStarted(event)) {
            throw new EventException("You can't delete event that already started.");
        }

        if (isEventEnded(event)) {
            throw new EventException("You can't delete event that already ended.");
        }

        if (LocalDateTime.now().isAfter(event.getStartDateTime().minusDays(3))) {
            throw new EventException("You can't delete event that starts in less than 3 days.");
        }
    }

    @Override
    public EventPurchaseDto reserveEvent(UUID id) {
        VisitorEntity newVisitor = authService.getVisitorForCurrentSession();

        EventEntity event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        validateReservation(event, newVisitor);

        var eventPurchase = calculateEventPurchasePrice(event, newVisitor);

        eventPurchaseService.save(eventPurchase);
        visitorService.save(newVisitor);
        this.save(event);

        return eventPurchaseMapper.toDto(eventPurchase);
    }

    private void cepRules(Collection<EventEntity> events,
                          Collection<EventPurchaseEntity> purchases,
                          Collection<EventAlterationLogEntity> eventAlterLogs) {
        KieContainer kieContainer = KnowledgeSessionHelper.createRuleBase();
        KieSession kSession = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "cep-session");
        LocalDateTime now = LocalDateTime.now();
        Collection<EventEntity> eventsToPromote = new HashSet<>();
        Collection<EventEntity> eventsToDelete = new HashSet<>();
        Collection<EventPurchaseEntity> purchasesToDelete = new HashSet<>();
        Collection<VisitorEntity> visitorsForMoneyReturn = new ArrayList<>();

        events.forEach(kSession::insert);
        purchases.forEach(kSession::insert);
        eventAlterLogs.forEach(kSession::insert);

        // Setting global variables for .drl file
        kSession.setGlobal("now", now);
        kSession.setGlobal("eventsToPromote", eventsToPromote);
        kSession.setGlobal("eventsToDelete", eventsToDelete);
        kSession.setGlobal("purchasesToDelete", purchasesToDelete);

        kSession.fireAllRules();

        purchasesToDelete.forEach(purchase -> {
            VisitorEntity visitor = purchase.getVisitor();
            visitor.setMoney(visitor.getMoney() + purchase.getPurchasePrice());
            mailService.sendTextEmail("Event cancelled", visitor.getEmail(),
                    "%s has been cancelled".formatted(purchase.getEvent().getName()));
            visitorsForMoneyReturn.add(visitor);
        });
        visitorsForMoneyReturn.forEach(visitorService::save);

        eventRepository.deleteAllInBatch(eventsToDelete);
        eventPurchaseService.deleteAllInBatch(purchasesToDelete);

        Collection<VisitorEntity> allVisitors = visitorService.getAll();
        for (EventEntity event : eventsToPromote) {
            allVisitors.forEach(visitor -> mailService.sendTextEmail("EVENT PROMOTION", visitor.getEmail(),
                    "We are promoting %s".formatted(event.getName())));
        }

        kSession.destroy();
    }

    private EventPurchaseEntity calculateEventPurchasePrice(EventEntity event, VisitorEntity visitor) {

        EventPurchaseEntity eventPurchase = EventPurchaseEntity.builder()
                .id(UUID.randomUUID())
                .event(event)
                .visitor(visitor)
                .status(EventPurchaseStatus.NOT_ENABLED)
                .purchasePrice(event.getPrice())
                .purchaseTime(LocalDateTime.now())
                .build();

        // template 1 (event capacity discount)
        KieSession discountPriceBasedOnCapacityKieSession = createKieSessionWithCompiledRulesForCapacityDiscount();
        discountPriceBasedOnCapacityKieSession.insert(visitor);
        discountPriceBasedOnCapacityKieSession.insert(event);
        discountPriceBasedOnCapacityKieSession.insert(eventPurchase);

        discountPriceBasedOnCapacityKieSession.fireAllRules();
        discountPriceBasedOnCapacityKieSession.destroy();

        // template 2 (event capacity scale up price)
        KieSession scaleUpPriceKieSession = createKieSessionWithCompiledRulesForCapacityScaleUp();
        scaleUpPriceKieSession.insert(visitor);
        scaleUpPriceKieSession.insert(event);
        scaleUpPriceKieSession.insert(eventPurchase);

        scaleUpPriceKieSession.fireAllRules();
        scaleUpPriceKieSession.destroy();

        // template 3 (seasonal changes discount)
        KieSession seasonalDiscountKieSession = createKieSessionWithCompiledRulesForSeasonalDiscount();
        WeatherBroadcast weatherBroadcast = weatherBroadcastGenerator.generateWeather(event.getStartDateTime().toLocalDate());
        seasonalDiscountKieSession.insert(weatherBroadcast);
        seasonalDiscountKieSession.insert(event);
        seasonalDiscountKieSession.insert(eventPurchase);
        seasonalDiscountKieSession.fireAllRules();
        seasonalDiscountKieSession.destroy();

        // forward chaining (reserve_event_rules)
        KieContainer kieContainer = KnowledgeSessionHelper.createRuleBase();
        KieSession kSession = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "test-k-session-3");

        kSession.insert(visitor);
        kSession.insert(event);
        kSession.insert(eventPurchase);
        kSession.fireAllRules();
        kSession.destroy();

        return eventPurchase;
    }

    private static KieSession createKieSessionWithCompiledRulesForCapacityDiscount() {
        InputStream templateStream = DefaultEventService.class.getResourceAsStream("/template/event_capacity_discount.drt");

        List<EventCapacityDiscount> data = new ArrayList<>();
        data.add(new EventCapacityDiscount(0, 100, 0.0));
        data.add(new EventCapacityDiscount(100, 1000, 0.10));
        data.add(new EventCapacityDiscount(1000, Integer.MAX_VALUE, 0.20));

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(data, templateStream);

        return createKieSessionFromDRL(drl);
    }

    private static KieSession createKieSessionWithCompiledRulesForCapacityScaleUp() {
        InputStream templateStream = DefaultEventService.class.getResourceAsStream("/template/event_capacity_scale_up_price.drt");

        List<EventScaleUpPrice> data = new ArrayList<>();
        data.add(new EventScaleUpPrice(-0.1, 0.8, 0.10));
        data.add(new EventScaleUpPrice(0.8, 0.9, 0.20));
        data.add(new EventScaleUpPrice(0.9, 1.0, 0.30));

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(data, templateStream);

        return createKieSessionFromDRL(drl);
    }

    private static KieSession createKieSessionWithCompiledRulesForSeasonalDiscount() {
        InputStream templateStream = DefaultEventService.class.getResourceAsStream("/template/event_seasonal_discount.drt");

        List<SeasonalDiscount> data = new ArrayList<>();

        // outdoor events
        data.add(new SeasonalDiscount(EventType.HIKING.name(), Double.MIN_VALUE, 30.0, PrecipitationType.NOTHING.name(), 0.15));
        data.add(new SeasonalDiscount(EventType.CYCLING.name(), Double.MIN_VALUE, 30.0, PrecipitationType.NOTHING.name(), 0.15));
        data.add(new SeasonalDiscount(EventType.PICNIC.name(), Double.MIN_VALUE, 30.0, PrecipitationType.NOTHING.name(), 0.15));
        data.add(new SeasonalDiscount(EventType.ZOO_VISIT.name(), Double.MIN_VALUE, 30.0, PrecipitationType.NOTHING.name(), 0.15));
        data.add(new SeasonalDiscount(EventType.THEME_PARK_VISIT.name(), Double.MIN_VALUE, 30.0, PrecipitationType.NOTHING.name(), 0.15));
        data.add(new SeasonalDiscount(EventType.FOOTBALL_MATCH.name(), Double.MIN_VALUE, 30.0, PrecipitationType.NOTHING.name(), 0.15));
        data.add(new SeasonalDiscount(EventType.PARAGLIDING.name(), Double.MIN_VALUE, 30.0, PrecipitationType.NOTHING.name(), 0.15));
        data.add(new SeasonalDiscount(EventType.BALLOON_RIDE.name(), Double.MIN_VALUE, 30.0, PrecipitationType.NOTHING.name(), 0.15));

        // no snow
        data.add(new SeasonalDiscount(EventType.WINTER_FESTIVAL.name(), Double.MAX_VALUE, Double.MIN_VALUE, PrecipitationType.NOTHING.name(), 0.2));
        data.add(new SeasonalDiscount(EventType.WINTER_FESTIVAL.name(), Double.MAX_VALUE, Double.MIN_VALUE, PrecipitationType.RAIN.name(), 0.2));

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String drl = compiler.compile(data, templateStream);

        return createKieSessionFromDRL(drl);
    }

    private void validateReservation(EventEntity event, VisitorEntity visitor) {
        if (!isEventHaveAvailableSeats(event)) throw new EventException("Event is full!");
        if (isEventAlreadyReservedByVisitor(event, visitor)) throw new EventException("You already reserved this event!");
        if (isEventEnded(event)) throw new EventException("Event already ended!");
        if (isEventStarted(event)) throw new EventException("Event already started!");
    }

    private EventEntity addVisitorToEvent(EventEntity event, VisitorEntity visitor) {
        var visitors = event.getVisitors();
        visitors.add(visitor);
        return event.toBuilder()
                .numberOfAvailableSeats(event.getNumberOfAvailableSeats() - 1)
                .visitors(visitors)
                .build();
    }

    @Override
    public EventResponseDto cancelReservation(UUID id) {
        VisitorEntity visitorToBeRemoved = authService.getVisitorForCurrentSession();

        EventEntity event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        validateCancellation(event, visitorToBeRemoved);
        EventPurchaseEntity purchaseToBeDeleted = eventPurchaseService
                .getEventPurchaseByEventIdAndVisitorId(event.getId(), visitorToBeRemoved.getId())
                .orElseThrow(() -> new EventException("Event purchase not found!"));

        EventEntity updatedEvent = removeVisitorFromEvent(event, visitorToBeRemoved);
        visitorToBeRemoved.setMoney(visitorToBeRemoved.getMoney() + purchaseToBeDeleted.getPurchasePrice());
        eventPurchaseService.deleteAllInBatch(List.of(purchaseToBeDeleted));
        updatedEvent = eventRepository.save(updatedEvent);
        visitorService.save(visitorToBeRemoved);
        return eventMapper.toDto(updatedEvent);
    }

    private EventEntity removeVisitorFromEvent(EventEntity event, VisitorEntity visitorToBeRemoved) {
        var visitors = event.getVisitors();
        visitors.remove(visitorToBeRemoved);

        return event.toBuilder()
                .numberOfAvailableSeats(event.getNumberOfAvailableSeats() + 1)
                .visitors(visitors)
                .build();
    }

    private void validateCancellation(EventEntity event, VisitorEntity visitor) {
        if (!isEventAlreadyReservedByVisitor(event, visitor)) {
            throw new EventException("You can't cancel reservation for event you didn't reserve.");
        }
        if (isEventStarted(event)) {
            throw new EventException("You can't cancel reservation for event that already started.");
        }
        if (isEventEnded(event)) {
            throw new EventException("You can't cancel reservation for event that already ended.");
        }

        if (LocalDateTime.now().isAfter(event.getStartDateTime().minusDays(1))) {
            throw new EventException("You can't cancel reservation for event that starts in less than 1 days.");
        }
    }

    @Override
    public Collection<EventResponseDto> getAllOrganizerFinishedEvents() {
        OrganizerEntity organizer = authService.getOrganizerForCurrentSession();
        var allOrganizerFinishedEvents = getAllOrganizerFinishedEvents(organizer);
        return eventMapper.toDto(allOrganizerFinishedEvents);
    }

    private List<EventEntity> getAllOrganizerFinishedEvents(OrganizerEntity organizer) {
        return eventRepository.findAllByOrganizerEmail(organizer.getEmail())
                .stream()
                .filter(this::isEventEnded)
                .toList();
    }

    @Override
    public Collection<EventResponseDto> getAllOrganizerPendingEvents() {
        OrganizerEntity organizer = authService.getOrganizerForCurrentSession();
        var allOrganizerPendingEvents = getAllOrganizerPendingEvents(organizer);
        return eventMapper.toDto(allOrganizerPendingEvents);
    }

    private List<EventEntity> getAllOrganizerPendingEvents(OrganizerEntity organizer) {
        return eventRepository.findAllByOrganizerEmail(organizer.getEmail())
                .stream()
                .filter(event -> !isEventStarted(event))
                .toList();
    }

    @Override
    public Collection<EventResponseDto> getAllVisitorVisitedEvents() {
        VisitorEntity visitor = authService.getVisitorForCurrentSession();
        var allVisitorVisitedEvents = getAllVisitorVisitedEvents(visitor);

        Collection<EventEntity> eventsWithUpdatedSalePrice = new ArrayList<>();
        for (EventEntity event : allVisitorVisitedEvents) {
            eventPurchaseService.getEventPurchaseByEventIdAndVisitorId(event.getId(), visitor.getId())
                    .ifPresent(eventPurchase -> event.setPrice(eventPurchase.getPurchasePrice()));
            eventsWithUpdatedSalePrice.add(event);
        }

        return eventMapper.toDto(eventsWithUpdatedSalePrice);
    }

    private List<EventEntity> getAllVisitorVisitedEvents(VisitorEntity visitor) {
        return eventRepository.findAllByVisitorsEmail(visitor.getEmail())
                .stream()
                .filter(this::isEventEnded)
                .toList();
    }

    @Override
    public Collection<EventResponseDto> getAllVisitorReservedEvents() {
        VisitorEntity visitor = authService.getVisitorForCurrentSession();
        var allVisitorReservedEvents = getAllVisitorReservedEvents(visitor);

        Collection<EventEntity> eventsWithUpdatedSalePrice = new ArrayList<>();
        for (EventEntity event : allVisitorReservedEvents) {
            eventPurchaseService.getEventPurchaseByEventIdAndVisitorId(event.getId(), visitor.getId())
                    .ifPresent(eventPurchase -> event.setPrice(eventPurchase.getPurchasePrice()));
            eventsWithUpdatedSalePrice.add(event);
        }

        return eventMapper.toDto(eventsWithUpdatedSalePrice);
    }

    private List<EventEntity> getAllVisitorReservedEvents(VisitorEntity visitor) {
        return eventRepository.findAllByVisitorsEmail(visitor.getEmail())
                .stream()
                .filter(event -> !isEventStarted(event))
                .toList();
    }

    @Override
    public Collection<EventResponseDto> getAllAvailableEvents() {
        VisitorEntity visitor = authService.getVisitorForCurrentSession();
        var allAvailableEvents = getAllAvailableEvents(visitor);

        Collection<EventEntity> eventsWithUpdatedSalePrice = new ArrayList<>();
        Collection<EventPurchaseEntity> purchases = new ArrayList<>();
        Collection<EventAlterationLogEntity> alterLogs = new ArrayList<>();
        for (EventEntity event : allAvailableEvents) {
            var clonedEvent = event.toBuilder().build();
            EventPurchaseEntity eventPurchase = calculateEventPurchasePrice(clonedEvent, visitor);
            event.setPrice(eventPurchase.getPurchasePrice());
            Collection<EventAlterationLogEntity> alterLogsForEvent = eventAlterationLogService.getForEvent(event.getId());
            Collection<EventPurchaseEntity> purchasesForEvent = eventPurchaseService.getEventPurchaseByEventId(event.getId());

            eventsWithUpdatedSalePrice.add(event);
            purchases.addAll(purchasesForEvent);
            alterLogs.addAll(alterLogsForEvent);
        }
//        cepRules(eventsWithUpdatedSalePrice, purchases, alterLogs);

        return eventMapper.toDto(eventsWithUpdatedSalePrice);
    }

    private Collection<EventEntity> getAllAvailableEvents(VisitorEntity visitor) {
        return eventRepository.findAll()
                .stream()
                .filter(event -> !isEventStarted(event) && isEventHaveAvailableSeats(event) && !isEventAlreadyReservedByVisitor(event, visitor))
                .toList();
    }

    @Override
    public Collection<EventResponseDto> getRecommendedEvents() {
        VisitorEntity visitor = authService.getVisitorForCurrentSession();
        Collection<EventEntity> availableEvents = getAllAvailableEvents(visitor);
        Collection<EventEntity> filteredEvents = new HashSet<>();

        KieContainer kieContainer = KnowledgeSessionHelper.createRuleBase();
        KieSession kSession = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "test-k-session-2"); // TODO promeniti
        kSession.setGlobal("filteredEvents", filteredEvents);

        kSession.insert(visitor);
        for (EventEntity event : availableEvents) {
            kSession.insert(event);
        }
        kSession.fireAllRules();

        Collection<EventEntity> eventsWithUpdatedSalePrice = new ArrayList<>();
        for (EventEntity event : filteredEvents) {
            var clonedEvent = event.toBuilder().build();
            EventPurchaseEntity eventPurchase = calculateEventPurchasePrice(clonedEvent, visitor);
            event.setPrice(eventPurchase.getPurchasePrice());
            eventsWithUpdatedSalePrice.add(event);
        }

        return eventMapper.toDto(eventsWithUpdatedSalePrice);
    }

    @Override
    public void sendPromotionMail(EventEntity event) {
        String subject = "Amazing promotion :D";
        String body = "Go to %s. It starts on %s\nWhen will you learn? WHEN WILL YOU LEARN!? THAT YOUR ACTIONS HAVE CONSEQUENCES!!!"
                .formatted(event.getType().name().replaceAll("_", " "), event.getStartDateTime().toLocalDate());
        Collection<VisitorEntity> visitors = visitorService.getAll();
        for (VisitorEntity visitor : visitors) {
            // TODO: Send promotion mail
        }
    }

    @Override
    public void cancelEvent(UUID id) {
        EventEntity event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        Collection<EventPurchaseEntity> purchases = eventPurchaseService.getEventPurchaseByEventId(event.getId());
        eventPurchaseService.deleteAllInBatch(purchases);
        eventRepository.delete(event);
    }

    private boolean hasOrganizerCreatedEvent(EventEntity event, OrganizerEntity organizerEntity) {
        return event.getOrganizer().equals(organizerEntity);
    }

    private boolean isEventHaveAvailableSeats(EventEntity event) {
        return event.getNumberOfAvailableSeats() > 0;
    }

    private boolean isEventAlreadyReservedByVisitor(EventEntity event, VisitorEntity visitor) {
        return event.getVisitors().contains(visitor);
    }

    private boolean isEventEnded(EventEntity event) {
        return event.getEndDateTime().isBefore(LocalDateTime.now());
    }

    private boolean isEventStarted(EventEntity event) {
        return event.getStartDateTime().isBefore(LocalDateTime.now());
    }
}
