package com.ftn.sbnz.app.feature.event.service.impl;

import com.ftn.sbnz.app.core.drools.KnowledgeSessionHelper;
import com.ftn.sbnz.app.core.other.exception.StartDateIsAfterEndDateException;
import com.ftn.sbnz.app.feature.auth.service.AuthService;
import com.ftn.sbnz.app.feature.event.dto.CreateUpdateEventRequestDto;
import com.ftn.sbnz.app.feature.event.dto.EventResponseDto;
import com.ftn.sbnz.app.feature.event.exception.EventException;
import com.ftn.sbnz.app.feature.event.exception.EventNotFoundException;
import com.ftn.sbnz.app.feature.event.mapper.EventMapper;
import com.ftn.sbnz.app.feature.event.repository.EventRepository;
import com.ftn.sbnz.app.feature.event.service.EventService;
import com.ftn.sbnz.model.core.OrganizerEntity;
import com.ftn.sbnz.model.core.visitor.VisitorEntity;
import com.ftn.sbnz.model.event.EventEntity;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DefaultEventService implements EventService {

    private final EventRepository eventRepository;

    private final AuthService authService;
    private final EventMapper eventMapper;

    @Override
    public EventEntity save(EventEntity eventEntity) {
        return eventRepository.save(eventEntity);
    }

    @Override
    public EventResponseDto getEventById(UUID id) {
        EventEntity eventEntity = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        return eventMapper.toDto(eventEntity);
    }

    @Override
    public EventResponseDto createEvent(CreateUpdateEventRequestDto dto) {
        OrganizerEntity organizer = authService.getOrganizerForCurrentSession();

        EventEntity eventToBeSaved = eventMapper.toEntity(dto, organizer);
        EventEntity savedEvent = eventRepository.save(eventToBeSaved);
        return eventMapper.toDto(savedEvent);
    }

    @Override
    public EventResponseDto updateEvent(UUID id, CreateUpdateEventRequestDto dto) {
        OrganizerEntity organizer = authService.getOrganizerForCurrentSession();

        EventEntity eventToUpdate = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        validateUpdate(eventToUpdate, organizer, dto);

        EventEntity updatedEvent = updateEvent(eventToUpdate, dto);
        eventRepository.save(updatedEvent);
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
    public EventResponseDto reserveEvent(UUID id) {
        VisitorEntity newVisitor = authService.getVisitorForCurrentSession();

        EventEntity event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        validateReservation(event, newVisitor);

        EventEntity updatedEvent = addVisitorToEvent(event, newVisitor);
        var savedEvent = eventRepository.save(updatedEvent);
        return eventMapper.toDto(savedEvent);
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

        EventEntity updatedEvent = removeVisitorFromEvent(event, visitorToBeRemoved);
        var savedEvent = eventRepository.save(updatedEvent);
        return eventMapper.toDto(savedEvent);
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
        return eventMapper.toDto(allVisitorVisitedEvents);
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
        return eventMapper.toDto(allVisitorReservedEvents);
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
        return eventMapper.toDto(allAvailableEvents);
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

        KieContainer kieContainer = KnowledgeSessionHelper.createRuleBase();
        KieSession kSession = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "test-k-session-1"); // TODO promeniti

        kSession.insert(visitor);
        for (EventEntity event : availableEvents) {
            kSession.insert(event);
        }
        kSession.fireAllRules();

        Collection<EventEntity> recommendedEvents = (Collection<EventEntity>) kSession.getObjects(new ClassObjectFilter(EventEntity.class));

        return eventMapper.toDto(recommendedEvents);
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
