package com.ftn.sbnz.app.feature.event.service;

import com.ftn.sbnz.app.feature.event.dto.CreateUpdateEventRequestDto;
import com.ftn.sbnz.app.feature.event.dto.EventResponseDto;
import com.ftn.sbnz.model.event.EventEntity;

import java.util.Collection;
import java.util.UUID;

public interface EventService {
    EventEntity save(EventEntity eventEntity);

    EventResponseDto getEventById(UUID id);

    EventResponseDto createEvent(CreateUpdateEventRequestDto dto);

    EventResponseDto updateEvent(UUID id, CreateUpdateEventRequestDto dto);

    EventResponseDto reserveEvent(UUID id);

    EventResponseDto cancelReservation(UUID id);

    void deleteEvent(UUID id);

    Collection<EventResponseDto> getAllOrganizerFinishedEvents();

    Collection<EventResponseDto> getAllOrganizerPendingEvents();

    Collection<EventResponseDto> getAllVisitorVisitedEvents();

    Collection<EventResponseDto> getAllVisitorReservedEvents();

    Collection<EventResponseDto> getAllAvailableEvents();
}
