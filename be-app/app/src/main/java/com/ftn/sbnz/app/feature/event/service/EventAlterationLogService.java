package com.ftn.sbnz.app.feature.event.service;

import com.ftn.sbnz.model.event.EventAlterationLogEntity;

import java.util.Collection;
import java.util.UUID;

public interface EventAlterationLogService {

    EventAlterationLogEntity saveAndGetEntity(EventAlterationLogEntity entity);
    Collection<EventAlterationLogEntity> getForEvent(UUID eventId);

}
