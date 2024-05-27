package com.ftn.sbnz.app.feature.event.repository;

import com.ftn.sbnz.model.event.EventAlterationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface EventAlterationLogRepository extends JpaRepository<EventAlterationLogEntity, UUID> {

    Collection<EventAlterationLogEntity> findByEventId(UUID eventId);

}
