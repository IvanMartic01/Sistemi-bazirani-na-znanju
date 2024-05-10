package com.ftn.sbnz.app.feature.event.controller;

import com.ftn.sbnz.app.feature.event.dto.CreateUpdateEventRequestDto;
import com.ftn.sbnz.app.feature.event.dto.EventResponseDto;
import com.ftn.sbnz.app.feature.event.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}")
    public EventResponseDto getEventById(@PathVariable UUID id) {
        return eventService.getEventById(id);
    }

    @PostMapping
    public EventResponseDto createEvent(@Valid @RequestBody CreateUpdateEventRequestDto dto) {
        return eventService.createEvent(dto);
    }

    @PutMapping("/{id}")
    public EventResponseDto updateEvent(@PathVariable UUID id, @RequestBody CreateUpdateEventRequestDto dto) {
        return eventService.updateEvent(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable UUID id) {
        eventService.deleteEvent(id);
    }

    @PutMapping("/{id}/reserve")
    public EventResponseDto reserveEvent(@PathVariable UUID id) {
        return  eventService.reserveEvent(id);
    }

    @PutMapping("/{id}/cancel-reservation")
    public EventResponseDto cancelReservation(@PathVariable UUID id) {
        return eventService.cancelReservation(id);
    }

    @GetMapping("/organizer-finished")
    public Collection<EventResponseDto> getOrganizerFinishedEvent() {
        return eventService.getAllOrganizerFinishedEvents();
    }

    @GetMapping("/organizer-pending")
    public Collection<EventResponseDto> getOrganizerPendingEvents() {
        return eventService.getAllOrganizerPendingEvents();
    }

    @GetMapping("/visitor-visited")
    public Collection<EventResponseDto> getVisitorVisitedEvent() {
        return eventService.getAllVisitorVisitedEvents();
    }

    @GetMapping("/visitor-reserved")
    public Collection<EventResponseDto> getVisitorReservedEvent() {
        return eventService.getAllVisitorReservedEvents();
    }

    @GetMapping("/visitor-available")
    public Collection<EventResponseDto> getAllAvailableEvents() {
        return eventService.getAllAvailableEvents();
    }
}
