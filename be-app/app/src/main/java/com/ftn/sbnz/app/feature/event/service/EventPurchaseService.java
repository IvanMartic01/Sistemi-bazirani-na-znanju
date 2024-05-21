package com.ftn.sbnz.app.feature.event.service;

import com.ftn.sbnz.model.event.EventPurchaseEntity;

import java.util.Optional;
import java.util.UUID;

public interface EventPurchaseService {
    Optional<EventPurchaseEntity> getEventPurchaseById(UUID id);
    Optional<EventPurchaseEntity> getEventPurchaseByEventIdAndVisitorId(UUID eventId, UUID visitorId);

    EventPurchaseEntity save(EventPurchaseEntity eventPurchaseEntity);
}
