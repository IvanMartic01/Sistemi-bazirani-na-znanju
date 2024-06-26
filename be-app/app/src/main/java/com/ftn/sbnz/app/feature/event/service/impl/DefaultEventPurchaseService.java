package com.ftn.sbnz.app.feature.event.service.impl;

import com.ftn.sbnz.app.feature.event.repository.EventPurchaseRepository;
import com.ftn.sbnz.app.feature.event.service.EventPurchaseService;
import com.ftn.sbnz.model.event.EventPurchaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DefaultEventPurchaseService implements EventPurchaseService {

    private final EventPurchaseRepository eventPurchaseRepository;

    @Override
    public Optional<EventPurchaseEntity> getEventPurchaseById(UUID id) {
        return eventPurchaseRepository.findById(id);
    }

    @Override
    public Optional<EventPurchaseEntity> getEventPurchaseByEventIdAndVisitorId(UUID eventId, UUID visitorId) {
        return eventPurchaseRepository.findByEventIdAndVisitorId(eventId, visitorId);
    }

    @Override
    public Collection<EventPurchaseEntity> getEventPurchaseByEventId(UUID eventId) {
        return eventPurchaseRepository.findByEventId(eventId);
    }

    @Override
    public void deleteAllInBatch(Collection<EventPurchaseEntity> purchases) {
        eventPurchaseRepository.deleteAllInBatch(purchases);
    }

    @Override
    public EventPurchaseEntity save(EventPurchaseEntity eventPurchaseEntity) {
        return eventPurchaseRepository.save(eventPurchaseEntity);
    }
}
