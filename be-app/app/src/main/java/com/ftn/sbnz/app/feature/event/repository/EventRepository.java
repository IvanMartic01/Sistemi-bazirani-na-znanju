package com.ftn.sbnz.app.feature.event.repository;


import com.ftn.sbnz.app.feature.event.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID> {

    Collection<EventEntity> findAllByVisitorsEmail(String email);
    Collection<EventEntity> findAllByOrganizerEmail(String email);
}
