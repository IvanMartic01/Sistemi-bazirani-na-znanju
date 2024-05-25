package com.ftn.sbnz.app.feature.event.service;

import com.ftn.sbnz.model.event.EventPurchaseEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface EventPurchaseService {
    Optional<EventPurchaseEntity> getEventPurchaseById(UUID id);
    Optional<EventPurchaseEntity> getEventPurchaseByEventIdAndVisitorId(UUID eventId, UUID visitorId);
    Collection<EventPurchaseEntity> getEventPurchaseByEventId(UUID eventId);
    void deleteAllInBatch(Collection<EventPurchaseEntity> purchases);

    EventPurchaseEntity save(EventPurchaseEntity eventPurchaseEntity);
}
